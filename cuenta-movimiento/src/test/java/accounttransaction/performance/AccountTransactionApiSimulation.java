package accounttransaction.performance;

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

public class AccountTransactionApiSimulation extends Simulation {

    private static final Duration USER_THINK_TIME = Duration.ofMillis(250);
    private static final String PERSON_ID = System.getProperty("gatling.personId", "11111111-1111-1111-1111-111111111111");
    private static final HttpClient WARMUP_CLIENT = HttpClient.newHttpClient();

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(GatlingSettings.baseUrl())
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling cuenta-movimiento");

    private final ScenarioBuilder accountFlow = scenario("account_transaction_flow")
            .exec(this::seedAccount)
            .exec(http("create_account")
                    .post("/api/cuentas")
                    .body(StringBody("""
                            {
                              "numero":"#{accountNumber}",
                              "saldoinicial":#{initialBalance},
                              "estado":true,
                              "tipo":"Ahorro",
                              "persona":"#{personId}"
                            }
                            """))
                    .asJson()
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("accountId")))
            .pause(USER_THINK_TIME)
            .exec(http("list_accounts")
                    .get("/api/cuentas")
                    .check(status().is(200)))
            .exec(http("get_account_by_id")
                    .get("/api/cuentas/#{accountId}")
                    .check(status().is(200))
                    .check(jsonPath("$.id").isEL("#{accountId}")))
            .pause(USER_THINK_TIME)
            .exec(http("deposit_movement")
                    .post("/api/movimientos")
                    .body(StringBody("""
                            {
                              "tipo":"Ahorro",
                              "numerocuenta":"#{accountNumber}",
                              "valormovimiento":250.0,
                              "estado":true
                            }
                            """))
                    .asJson()
                    .check(status().is(201))
                    .check(jsonPath("$.detalle").exists()))
            .pause(USER_THINK_TIME)
            .exec(http("withdrawal_movement")
                    .post("/api/movimientos")
                    .body(StringBody("""
                            {
                              "tipo":"Ahorro",
                              "numerocuenta":"#{accountNumber}",
                              "valormovimiento":-120.0,
                              "estado":true
                            }
                            """))
                    .asJson()
                    .check(status().is(201))
                    .check(jsonPath("$.detalle").exists()))
            .pause(USER_THINK_TIME)
            .exec(http("list_movements")
                    .get("/api/movimientos")
                    .check(status().is(200)));

    {
        setUp(accountFlow.injectOpen(
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
        warmUp("/api/cuentas");
    }

    private Session seedAccount(Session session) {
        String uniqueSuffix = System.currentTimeMillis() + "-" + ThreadLocalRandom.current().nextInt(1_000_000);
        return session
                .set("accountNumber", "ACC-" + uniqueSuffix)
                .set("initialBalance", "1000.0")
                .set("personId", PERSON_ID);
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
