# Java Spring Boot Finance Backend

Repositorio con dos servicios Spring Boot para la prueba de clientes, cuentas, movimientos y reportes financieros.

## Servicios

| Servicio | Puerto | Swagger | Endpoints principales |
| --- | --- | --- | --- |
| `cliente-persona` | `1203` | `http://localhost:1203/swagger-ui/index.html` | `/api/clients` |
| `cuenta-movimiento` | `1204` | `http://localhost:1204/swagger-ui/index.html` | `/api/cuentas`, `/api/movimientos`, `/api/reports` |

## Stack actual

- Java `25`
- Spring Boot `4.0.3`
- Maven Wrapper por módulo
- PostgreSQL en Docker
- H2 para tests
- Karate, Gatling y JaCoCo
- Workflow de validación en [`.github/workflows/validate.yml`](.github/workflows/validate.yml)

## Estructura

- [`cliente-persona/`](cliente-persona): servicio de clientes/personas
- [`cuenta-movimiento/`](cuenta-movimiento): servicio de cuentas, movimientos y reportes
- [`BD_SCRIPTS/`](BD_SCRIPTS): scripts iniciales de base de datos
- [`postman/`](postman): colección agregada del sistema

## Levantar todo con Docker

Los scripts de [`BD_SCRIPTS/`](BD_SCRIPTS) se cargan automáticamente cuando inicia PostgreSQL.

```bash
docker compose up --build
```

Servicios disponibles:

- PostgreSQL: `localhost:65432`
- `cliente-persona`: `http://localhost:1203`
- `cuenta-movimiento`: `http://localhost:1204`

## Ejecutar localmente con Java 25

1. Levanta PostgreSQL:

```bash
docker compose up -d postgres
```

2. Activa Java 25 con SDKMAN:

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 25.0.2-tem
```

3. Ejecuta `cliente-persona`:

```bash
cd cliente-persona
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:65432/postgres \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
./mvnw spring-boot:run
```

4. En otra terminal, ejecuta `cuenta-movimiento`:

```bash
cd cuenta-movimiento
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:65432/postgres \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
CLIENT_PERSONA_BASE_URL=http://localhost:1203 \
./mvnw spring-boot:run
```

## Validación local

Ejecuta estos comandos dentro de cada módulo:

```bash
./mvnw clean test -DexcludedGroups=karate
./mvnw -Dtest=karate.ApiContractsKarateTest test
./mvnw -Pcoverage verify -DexcludedGroups=karate
./mvnw -Pgatling verify -DskipTests=true
```

## Postman

Colecciones actualizadas:

- [`postman/java-spb-finance-backend.postman_collection.json`](postman/java-spb-finance-backend.postman_collection.json)
- [`cliente-persona/postman/cliente-persona.postman_collection.json`](cliente-persona/postman/cliente-persona.postman_collection.json)
- [`cuenta-movimiento/postman/cuenta-movimiento.postman_collection.json`](cuenta-movimiento/postman/cuenta-movimiento.postman_collection.json)

La colección raíz usa variables para `clientBaseUrl`, `accountBaseUrl`, `clientId`, `personId`, `accountId`, `accountNumber` y `reportDate`.
