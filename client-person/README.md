# Client Person Service

Spring Boot service for managing clients and persons.

## Port and docs

- Port: `1203`
- Swagger: `http://localhost:1203/swagger-ui/index.html`
- Base path: `/api/clients`

## Run locally

Requires Java `25` and PostgreSQL on `localhost:65432`.

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 25.0.2-tem

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:65432/postgres \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
./mvnw spring-boot:run
```

## Run with Docker

From the repository root:

```bash
docker compose up --build client-person
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

- [`postman/client-person.postman_collection.json`](postman/client-person.postman_collection.json)
