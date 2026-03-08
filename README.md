# Java Spring Boot Finance Backend

Repository with two Spring Boot services for clients, accounts, movements, and financial reports.

## Services

| Service | Port | Swagger | Main endpoints |
| --- | --- | --- | --- |
| `client-person` | `1203` | `http://localhost:1203/swagger-ui/index.html` | `/api/clients` |
| `account-transaction` | `1204` | `http://localhost:1204/swagger-ui/index.html` | `/api/accounts`, `/api/movements`, `/api/reports` |

## Current stack

- Java `25`
- Spring Boot `4.0.3`
- Maven Wrapper per module
- PostgreSQL in Docker
- H2 for tests
- Karate, Gatling, and JaCoCo
- Validation workflow in [`.github/workflows/validate.yml`](.github/workflows/validate.yml)

## Structure

- [`client-person/`](client-person): client and person service
- [`account-transaction/`](account-transaction): account, movement, and report service
- [`DB_SCRIPTS/`](DB_SCRIPTS): database bootstrap scripts
- [`postman/`](postman): aggregated API collection

## Start everything with Docker

The scripts in [`DB_SCRIPTS/`](DB_SCRIPTS) are loaded automatically when PostgreSQL starts.

```bash
docker compose up --build
```

Available services:

- PostgreSQL: `localhost:65432`
- `client-person`: `http://localhost:1203`
- `account-transaction`: `http://localhost:1204`

## Run locally with Java 25

1. Start PostgreSQL:

```bash
docker compose up -d postgres
```

2. Activate Java 25 with SDKMAN:

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 25.0.2-tem
```

3. Run `client-person`:

```bash
cd client-person
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:65432/postgres \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
./mvnw spring-boot:run
```

4. In another terminal, run `account-transaction`:

```bash
cd account-transaction
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:65432/postgres \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
CLIENT_PERSON_BASE_URL=http://localhost:1203 \
./mvnw spring-boot:run
```

## Local validation

Run these commands inside each module:

```bash
./mvnw clean test -DexcludedGroups=karate
./mvnw -Dtest=karate.ApiContractsKarateTest test
./mvnw -Pcoverage verify -DexcludedGroups=karate
./mvnw -Pgatling verify -DskipTests=true
```

## Postman

Updated collections:

- [`postman/java-spb-finance-backend.postman_collection.json`](postman/java-spb-finance-backend.postman_collection.json)
- [`client-person/postman/client-person.postman_collection.json`](client-person/postman/client-person.postman_collection.json)
- [`account-transaction/postman/account-transaction.postman_collection.json`](account-transaction/postman/account-transaction.postman_collection.json)

The root collection uses `clientBaseUrl`, `accountBaseUrl`, `clientId`, `accountId`, `accountNumber`, and `reportDate`.
