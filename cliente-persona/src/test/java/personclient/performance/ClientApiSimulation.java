package personclient.performance;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

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

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(GatlingSettings.baseUrl())
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling cliente-persona");

    private final ScenarioBuilder clientCrudFlow = scenario("client_crud_flow")
            .exec(this::seedClient)
            .exec(http("create_client")
                    .post("/api/clients")
                    .body(StringBody("""
                            {
                              "nombre":"#{name}",
                              "clienteid":"#{clientId}",
                              "contrasena":"#{password}",
                              "genero":"#{gender}",
                              "edad":#{age},
                              "identificacion":"#{identifier}",
                              "direccion":"#{address}",
                              "telefono":"#{phone}",
                              "estado":"#{status}"
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
                              "nombre":"#{updatedName}",
                              "clienteid":"#{updatedClientId}",
                              "contrasena":"#{updatedPassword}",
                              "genero":"#{updatedGender}",
                              "edad":#{updatedAge},
                              "identificacion":"#{updatedIdentifier}",
                              "direccion":"#{updatedAddress}",
                              "telefono":"#{updatedPhone}",
                              "estado":"#{updatedStatus}"
                            }
                            """))
                    .asJson()
                    .check(status().is(200))
                    .check(jsonPath("$.id").isEL("#{persistedId}"))
                    .check(jsonPath("$.nombre").isEL("#{updatedName}")))
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
                .set("status", "ACTIVO")
                .set("updatedName", "Updated Client " + uniqueSuffix)
                .set("updatedClientId", "updated-client-" + uniqueSuffix)
                .set("updatedPassword", "Updated123")
                .set("updatedGender", "F")
                .set("updatedAge", "31")
                .set("updatedIdentifier", "updated-" + uniqueSuffix + "@example.com")
                .set("updatedAddress", "Updated Street " + uniqueSuffix)
                .set("updatedPhone", "+56987654321")
                .set("updatedStatus", "ACTIVO");
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
}
