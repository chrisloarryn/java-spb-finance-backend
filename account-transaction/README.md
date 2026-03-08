# Account Transaction Service

Spring Boot service for accounts, movements, and client reports.

## Port and docs

- Port: `1204`
- Swagger: `http://localhost:1204/swagger-ui/index.html`
- Base paths:
  - `/api/accounts`
  - `/api/movements`
  - `/api/reports`

## External dependency

The report endpoint calls `client-person` through the `client.person.base-url` property.

Local example:

```bash
CLIENT_PERSON_BASE_URL=http://localhost:1203
```

## Run locally

Requires Java `25`, PostgreSQL on `localhost:65432`, and a running `client-person` service.

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 25.0.2-tem

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:65432/postgres \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
CLIENT_PERSON_BASE_URL=http://localhost:1203 \
./mvnw spring-boot:run
```

## Run with Docker

From the repository root:

```bash
docker compose up --build account-transaction
```

## Validation

```bash
./mvnw clean test -DexcludedGroups=karate
./mvnw -Dtest=karate.ApiContractsKarateTest test
./mvnw -Pcoverage verify -DexcludedGroups=karate
./mvnw -Pgatling verify -DskipTests=true
```

## Postman

Module collection:

- [`postman/account-transaction.postman_collection.json`](postman/account-transaction.postman_collection.json)
