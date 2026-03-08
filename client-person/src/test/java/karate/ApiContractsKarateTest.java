package karate;

import java.util.Arrays;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import personclient.ClientPersonApiApplication;

@SpringBootTest(classes = ClientPersonApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("karate")
class ApiContractsKarateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void configureBaseUrl() {
        System.setProperty("karate.baseUrl", "http://127.0.0.1:" + port);
    }

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM persons");
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
}
