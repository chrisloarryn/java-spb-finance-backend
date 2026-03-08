# Cuenta Movimiento Service

Servicio Spring Boot para cuentas, movimientos y reportes por cliente.

## Puerto y documentación

- Puerto: `1204`
- Swagger: `http://localhost:1204/swagger-ui/index.html`
- Base paths:
  - `/api/cuentas`
  - `/api/movimientos`
  - `/api/reports`

## Dependencia externa

El endpoint de reportes consulta `cliente-persona` mediante la propiedad `client.persona.base-url`.

Ejemplo local:

```bash
CLIENT_PERSONA_BASE_URL=http://localhost:1203
```

## Ejecutar localmente

Requiere Java `25`, PostgreSQL en `localhost:65432` y `cliente-persona` corriendo.

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 25.0.2-tem

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:65432/postgres \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
CLIENT_PERSONA_BASE_URL=http://localhost:1203 \
./mvnw spring-boot:run
```

## Ejecutar con Docker

Desde la raíz del repo:

```bash
docker compose up --build cuenta-movimiento
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

- [`postman/cuenta-movimiento.postman_collection.json`](postman/cuenta-movimiento.postman_collection.json)
