package personclient.performance;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.forAll;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ClientApiSimulation extends Simulation {

    private static final Duration USER_THINK_TIME = Duration.ofMillis(250);
    private static final HttpClient WARMUP_CLIENT = HttpClient.newHttpClient();
    private static final Pattern ID_PATTERN = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"");

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(GatlingSettings.baseUrl())
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling client-person");

    private final ScenarioBuilder clientCrudFlow = scenario("client_crud_flow")
            .exec(this::seedClient)
            .exec(http("create_client")
                    .post("/api/clients")
                    .body(StringBody("""
                            {
                              "name":"#{name}",
                              "clientId":"#{clientId}",
                              "password":"#{password}",
                              "gender":"#{gender}",
                              "age":#{age},
                              "identifier":"#{identifier}",
                              "address":"#{address}",
                              "phoneNumber":"#{phone}",
                              "status":"#{status}"
                            }
                            """))
                    .asJson()
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("persistedId")))
            .pause(USER_THINK_TIME)
            .exec(http("list_clients")
                    .get("/api/clients")
                    .check(status().is(200)))
            .exec(http("get_client_by_id")
                    .get("/api/clients/#{persistedId}")
                    .check(status().is(200))
                    .check(jsonPath("$.id").isEL("#{persistedId}")))
            .pause(USER_THINK_TIME)
            .exec(http("update_client")
                    .put("/api/clients/#{persistedId}")
                    .body(StringBody("""
                            {
                              "name":"#{updatedName}",
                              "clientId":"#{updatedClientId}",
                              "password":"#{updatedPassword}",
                              "gender":"#{updatedGender}",
                              "age":#{updatedAge},
                              "identifier":"#{updatedIdentifier}",
                              "address":"#{updatedAddress}",
                              "phoneNumber":"#{updatedPhone}",
                              "status":"#{updatedStatus}"
                            }
                            """))
                    .asJson()
                    .check(status().is(200))
                    .check(jsonPath("$.id").isEL("#{persistedId}"))
                    .check(jsonPath("$.name").isEL("#{updatedName}")))
            .pause(USER_THINK_TIME)
            .exec(http("delete_client")
                    .delete("/api/clients/#{persistedId}")
                    .check(status().is(204)));

    {
        setUp(clientCrudFlow.injectOpen(
                        rampUsers(GatlingSettings.users()).during(GatlingSettings.rampDuration()),
                        constantUsersPerSec(GatlingSettings.users()).during(GatlingSettings.holdDuration())))
                .protocols(httpProtocol)
                .assertions(
                        global().failedRequests().count().is(0L),
                        global().responseTime().percentile3().lt(1500),
                        forAll().successfulRequests().percent().is(100.0));
    }

    @Override
    public void before() {
        warmUp("/api/clients");
        warmUpCrudFlow();
    }

    private Session seedClient(Session session) {
        String uniqueSuffix = System.currentTimeMillis() + "-" + ThreadLocalRandom.current().nextInt(1_000_000);
        return session
                .set("name", "Gatling Client " + uniqueSuffix)
                .set("clientId", "gatling-client-" + uniqueSuffix)
                .set("password", "Secret123")
                .set("gender", "M")
                .set("age", "30")
                .set("identifier", "gatling-" + uniqueSuffix + "@example.com")
                .set("address", "Street " + uniqueSuffix)
                .set("phone", "+56912345678")
                .set("status", "ACTIVE")
                .set("updatedName", "Updated Client " + uniqueSuffix)
                .set("updatedClientId", "updated-client-" + uniqueSuffix)
                .set("updatedPassword", "Updated123")
                .set("updatedGender", "F")
                .set("updatedAge", "31")
                .set("updatedIdentifier", "updated-" + uniqueSuffix + "@example.com")
                .set("updatedAddress", "Updated Street " + uniqueSuffix)
                .set("updatedPhone", "+56987654321")
                .set("updatedStatus", "ACTIVE");
    }

    private void warmUp(String path) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(GatlingSettings.baseUrl() + path))
                .header("Accept", "application/json")
                .GET()
                .build();

        int attempts = 0;
        while (attempts < 10) {
            attempts++;
            try {
                HttpResponse<Void> response = WARMUP_CLIENT.send(request, HttpResponse.BodyHandlers.discarding());
                if (response.statusCode() >= 200 && response.statusCode() < 500) {
                    return;
                }
            } catch (IOException exception) {
                // Retry while the embedded server finishes bootstrapping.
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Warm-up interrupted for " + path, exception);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Warm-up interrupted for " + path, exception);
            }
        }

        throw new IllegalStateException("Warm-up failed for " + path);
    }

    private void warmUpCrudFlow() {
        String uniqueSuffix = "warmup-" + System.currentTimeMillis();

        HttpResponse<String> createResponse = sendJsonRequest(
                "POST",
                "/api/clients",
                """
                        {
                          "name":"Warmup Client %s",
                          "clientId":"warmup-client-%s",
                          "password":"Secret123",
                          "gender":"M",
                          "age":30,
                          "identifier":"warmup-%s@example.com",
                          "address":"Warmup Street %s",
                          "phoneNumber":"+56912345678",
                          "status":"ACTIVE"
                        }
                        """.formatted(uniqueSuffix, uniqueSuffix, uniqueSuffix, uniqueSuffix),
                201);

        String persistedId = extractPersistedId(createResponse.body());

        sendRequest("GET", "/api/clients", 200);
        sendRequest("GET", "/api/clients/" + persistedId, 200);
        sendJsonRequest(
                "PUT",
                "/api/clients/" + persistedId,
                """
                        {
                          "name":"Warmup Client Updated %s",
                          "clientId":"warmup-client-updated-%s",
                          "password":"Updated123",
                          "gender":"F",
                          "age":31,
                          "identifier":"warmup-updated-%s@example.com",
                          "address":"Warmup Avenue %s",
                          "phoneNumber":"+56987654321",
                          "status":"ACTIVE"
                        }
                        """.formatted(uniqueSuffix, uniqueSuffix, uniqueSuffix, uniqueSuffix),
                200);
        sendRequest("DELETE", "/api/clients/" + persistedId, 204);
    }

    private HttpResponse<String> sendJsonRequest(String method, String path, String body, int expectedStatus) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(GatlingSettings.baseUrl() + path))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .build();

        return sendWarmupRequest(request, expectedStatus);
    }

    private HttpResponse<String> sendRequest(String method, String path, int expectedStatus) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(GatlingSettings.baseUrl() + path))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();

        return sendWarmupRequest(request, expectedStatus);
    }

    private HttpResponse<String> sendWarmupRequest(HttpRequest request, int expectedStatus) {
        try {
            HttpResponse<String> response = WARMUP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != expectedStatus) {
                throw new IllegalStateException(
                        "Warm-up request failed for %s %s. Expected %d but got %d. Body: %s".formatted(
                                request.method(),
                                request.uri(),
                                expectedStatus,
                                response.statusCode(),
                                response.body()));
            }
            return response;
        } catch (IOException exception) {
            throw new IllegalStateException("Warm-up request failed for " + request.uri(), exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Warm-up interrupted for " + request.uri(), exception);
        }
    }

    private String extractPersistedId(String body) {
        Matcher matcher = ID_PATTERN.matcher(body);
        if (!matcher.find()) {
            throw new IllegalStateException("Warm-up create response did not include an id: " + body);
        }
        return matcher.group(1);
    }
}
