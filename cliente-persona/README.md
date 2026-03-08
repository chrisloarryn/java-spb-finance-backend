# Cliente Persona Service

Servicio Spring Boot para administración de clientes/personas.

## Puerto y documentación

- Puerto: `1203`
- Swagger: `http://localhost:1203/swagger-ui/index.html`
- Base path: `/api/clients`

## Ejecutar localmente

Requiere Java `25` y PostgreSQL disponible en `localhost:65432`.

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 25.0.2-tem

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:65432/postgres \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
./mvnw spring-boot:run
```

## Ejecutar con Docker

Desde la raíz del repo:

```bash
docker compose up --build cliente-persona
```

## Validación

```bash
./mvnw clean test -DexcludedGroups=karate
./mvnw -Dtest=karate.ApiContractsKarateTest test
./mvnw -Pcoverage verify -DexcludedGroups=karate
./mvnw -Pgatling verify -DskipTests=true
```

## Postman

Colección del módulo:

- [`postman/cliente-persona.postman_collection.json`](postman/cliente-persona.postman_collection.json)
