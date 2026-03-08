package karate;

import java.util.Arrays;
import java.util.UUID;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import accounttransaction.AccountTransactionApiApplication;

@SpringBootTest(classes = AccountTransactionApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("karate")
class ApiContractsKarateTest {

    private static final UUID FIXED_CLIENT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final WireMockServer WIREMOCK = new WireMockServer(wireMockConfig().dynamicPort());

    static {
        WIREMOCK.start();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("client.person.base-url", WIREMOCK::baseUrl);
    }

    @BeforeAll
    void configureBaseUrl() {
        System.setProperty("karate.baseUrl", "http://127.0.0.1:" + port);
        System.setProperty("karate.clientId", FIXED_CLIENT_ID.toString());
    }

    @AfterAll
    void clearProperties() {
        System.clearProperty("karate.baseUrl");
        System.clearProperty("karate.clientId");
        WIREMOCK.stop();
    }

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM movements");
        jdbcTemplate.execute("DELETE FROM accounts");
        stubClient();
    }

    @Karate.Test
    Karate contracts() {
        Karate runner = new Karate().path("classpath:karate/contracts");
        String configuredTags = System.getProperty("karate.tags");
        if (configuredTags == null || configuredTags.isBlank()) {
            return runner.tags("@regression");
        }
        return runner.tags(Arrays.stream(configuredTags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isBlank())
                .toArray(String[]::new));
    }

    private void stubClient() {
        WIREMOCK.resetAll();
        WIREMOCK.stubFor(get(urlEqualTo("/api/clients/" + FIXED_CLIENT_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "id":"11111111-1111-1111-1111-111111111111",
                                  "name":"Stub Client",
                                  "clientId":"stub-client",
                                  "password":"secret",
                                  "gender":"M",
                                  "age":30,
                                  "identifier":"stub@example.com",
                                  "address":"Stub Street 123",
                                  "phoneNumber":"+56999999999",
                                  "status":"ACTIVE",
                                  "createdAt":"2026-03-08T00:00:00Z"
                                }
                                """)));
    }
}
