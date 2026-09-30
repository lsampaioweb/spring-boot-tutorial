<!-- filepath: README.md -->
# Java Libraries and Spring Boot Projects

Documentation and working samples for building Java libraries and Spring Boot projects following modern architecture and best practices.

**Project Specifications:**
- **Spring Boot:** 4.1.1
- **Java:** 25
- **Maven:** 3.9+

## Project Guidelines & Conventions

This project follows standardized Spring Boot architecture and code conventions:

- **Feature-based packaging:** Code organized by feature, not layer (e.g., `user`, `order` rather than `service`, `repository`)
- **DTOs for API boundaries:** Requests and responses use dedicated DTO classes, not domain objects
- **Service layer pattern:** Interface + implementation (`Service.java` / `ServiceImpl.java`)
- **i18n for messages:** All user-facing messages externalized to `messages.properties`
- **Exception handling:** Centralized `@RestControllerAdvice` with domain exception hierarchy
- **Security:** Deny-by-default policies with `@PreAuthorize` method-level guards
- **Validation:** Input validation via `spring-boot-starter-validation` with `@Valid` on controllers
- **Database access:** Spring JDBC / MyBatis (no ORM)
- **OpenAPI documentation:** REST-focused samples expose Swagger UI through `springdoc-openapi` (enabled in development, disabled in production)
- **Testing:** Slice tests (`@WebMvcTest`, `@DataJdbcTest`) + integration tests

For detailed conventions, see the instruction files in: `https://github.com/lsampaioweb/ai-instructions`

### Ubuntu Environment:
1. [Install Java](documentation/java/install.md):
    - Instructions on installing Java on Ubuntu.
1. [Install Maven](documentation/maven/install.md):
    - Steps to install Maven.
1. [Install VSCode Extensions](documentation/vscode/index.md):
    - Recommended VSCode extensions for Java development.

### Setup:
1. [Create a Spring Boot Project](documentation/setup/project.md):
    - Guide to set up a Spring Boot project using VS Code or CLI.

### Spring Boot Basics:
1. [Maven Commands](documentation/maven/pom.md):
    - Common Maven commands and usage. Sample: `samples/01-pom`.
1. [Upgrade Process](documentation/maven/upgrade.md):
    - Practical workflow to keep all sample POMs updated.
1. [DevTools](documentation/spring/basic/devtools.md):
    - Enabling and using Spring Boot DevTools. Sample: `samples/02-devtools`.
1. [Profile](documentation/spring/basic/profile.md)
    - Using Spring Boot profiles. Sample: `samples/03-profiles`.
1. [Logs](documentation/spring/basic/logs.md):
    - Configuring and managing logs in Spring Boot. Sample: `samples/04-logs`.
1. [Lombok](documentation/spring/basic/lombok.md)
    - Integrating Lombok into your Spring Boot project. Sample: `samples/05-lombok`.
1. [i18n](documentation/spring/intermediate/i18n.md)
    - MessageSource bundles first, then HTTP locale on REST. Samples: `samples/06-i18n`, `samples/08-restapi`.
1. [Actuator](documentation/spring/basic/actuator.md)
    - Monitoring and managing your Spring Boot application. Sample: `samples/07-actuator`.

### Spring Boot Intermediate:
1. [REST](documentation/spring/basic/rest.md)
    - Creating RESTful web services, pagination, sorting, and OpenAPI. Sample: `samples/08-restapi`.
1. [Validation](documentation/spring/intermediate/validation.md)
    - Input validation for REST APIs and web forms. Sample: `samples/09-validation`.
1. [Exception Handling](documentation/spring/intermediate/exception-handling.md)
    - Handling exceptions in Spring Boot applications. Sample: `samples/10-exception-handling`.
1. [MapStruct](documentation/spring/intermediate/mapstruct.md)
    - Compile-time DTO mapping generation. Sample: `samples/11-mapstruct`.
1. [HTTP Client](documentation/spring/intermediate/http-client.md)
    - Making HTTP requests with RestClient. Sample: `samples/12-http-client`.
1. [Thymeleaf](documentation/spring/basic/thymeleaf.md)
    - Server-side HTML rendering with form binding and i18n support. Sample: `samples/13-thymeleaf`.
1. [HTTPS](documentation/spring/intermediate/https.md)
    - Securing your application with HTTPS. Sample: `samples/14-https`.
1. [Events](documentation/spring/intermediate/events.md)
    - Spring Application Events and async listeners. Sample: `samples/15-events`.
1. [Async](documentation/spring/intermediate/async.md)
    - `@EnableAsync`, `@Async` workers, and accept-and-poll job APIs. Sample: `samples/27-async/basics`.

### Spring Boot Advanced:
1. [Virtual Threads](documentation/spring/advanced/virtual-threads.md)
    - Using virtual threads in Spring Boot. Sample: `samples/16-virtual-threads`.
1. [Container](documentation/spring/extra/container.md)
    - Containerizing your Spring Boot application with Docker. Sample: `samples/17-container`.
1. [Security](documentation/spring/advanced/security.md)
    - Securing your Spring Boot application. Sample: `samples/18-security`.
1. [Cloud Config](documentation/spring/advanced/cloud-config.md)
    - Externalized configuration using Spring Cloud Config. Sample: `samples/19-cloud-config`.
1. [K6](documentation/spring/tests/k6.md)
    - Performance testing with K6. Catalog: `samples/20-k6`. App-shaped scripts: `samples/16-virtual-threads/src/test/k6`.

### Spring Boot Integrations:
1. [PostgreSQL](documentation/spring/integrations/postgresql.md)
    - Three sub-projects: `crud`, `batch`, `transactions`. Infrastructure runbook: `samples/infrastructure/postgres/README.md`. Sample: `samples/21-postgres`.
1. [RabbitMQ](documentation/spring/integrations/rabbitmq.md)
    - Messaging with RabbitMQ (direct, fanout, topic, headers exchanges). Infrastructure runbook: `samples/infrastructure/rabbitmq/README.md`. Sample: `samples/22-rabbitmq`.
1. [Redis](documentation/spring/integrations/redis.md)
    - Three sub-projects: `datastore`, `cache-layer`, `pubsub-events`. Infrastructure runbook: `samples/infrastructure/redis/README.md`. Sample: `samples/23-redis`.
1. [Vault](documentation/spring/integrations/vault.md)
    - Three sub-projects: `single-secret`, `multiple-secrets`, `secret-rotation`. Infrastructure runbook: `samples/infrastructure/vault/README.md`. Sample: `samples/24-vault`.
1. [WebSocket](documentation/spring/advanced/websocket.md)
    - Implementing WebSocket communication. Sample: `samples/25-websocket`.
1. [Traefik](documentation/spring/integrations/traefik.md)
    - Reverse proxy for container routing. Infrastructure runbook: `samples/infrastructure/traefik/README.md`. Sample: `samples/26-traefik`.
1. [Tracing](documentation/spring/integrations/tracing.md)
    - Cross-JVM W3C propagation with OTLP export to the Collector. Infrastructure runbook: `samples/infrastructure/opentelemetry/README.md`. Sample: `samples/28-tracing`.

## Infrastructure Services

All integration samples use containerized infrastructure defined in `samples/infrastructure/`:
- **PostgreSQL** — Relational database
- **RabbitMQ** — Message broker
- **Redis** — In-memory data store
- **Traefik** — Reverse proxy for HTTP routing and load balancing
- **Vault** — HashiCorp Vault for secrets management
- **OpenTelemetry Collector** — OTLP receiver that prints spans to stdout for the tracing sample

Each service runs in Compose with security hardening (dropped capabilities and no-new-privileges).

To use any integration:
1. Create shared network once if it does not exist (Docker): `docker network create tutorial-network`. Starting Traefik, Vault, Redis, PostgreSQL, RabbitMQ, or the OpenTelemetry Collector also creates it.
2. Open the service folder: `cd samples/infrastructure/{service}`.
3. If available, copy secret defaults: `cp .env.example .env`.
4. Start infrastructure: `docker compose up -d`.
5. Stop infrastructure: `docker compose down`.

If you use Podman, replace step 1 with `podman network exists tutorial-network || podman network create tutorial-network`, then replace `docker compose` with `podman compose`.

If `docker compose up` fails with `docker-credential-secretservice` missing, install Docker credential helpers or remove the `credsStore` setting from `~/.docker/config.json`.

If `podman compose up` fails with errors like `potentially insufficient UIDs or GIDs available in user namespace`, your user is missing rootless mappings in `/etc/subuid` and `/etc/subgid`. Ask an administrator to provision subuid/subgid ranges for your user, then run `podman system migrate` and retry.

Podman rootless preflight check:

```bash
whoami
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```

If either grep returns no line, Podman rootless mappings are not configured for your user.

Administrator remediation example (run as root, with unique ranges):

```bash
usermod --add-subuids 100000-165535 --add-subgids 100000-165535 <username>
```

Then the developer should run:

```bash
podman system migrate
```

See individual integration documentation for detailed setup steps.

### Swagger UI (Development Profile)
The following samples expose Swagger UI when running with the `development` profile:

- `samples/08-restapi` → `http://localhost:8080/swagger-ui/index.html`
- `samples/09-validation` → `http://localhost:8080/swagger-ui/index.html`
- `samples/10-exception-handling` → `http://localhost:8080/swagger-ui/index.html`
- `samples/11-mapstruct` → `http://localhost:8080/swagger-ui/index.html`
- `samples/12-http-client` → `http://localhost:8080/swagger-ui/index.html`
- `samples/15-events` → `http://localhost:8080/swagger-ui/index.html`
- `samples/16-virtual-threads` → `http://localhost:8080/swagger-ui/index.html`
- `samples/17-container` → `http://localhost:8080/swagger-ui/index.html`
- `samples/18-security` → `http://localhost:8080/swagger-ui/index.html`
- `samples/19-cloud-config/client` → `http://localhost:8080/swagger-ui/index.html`
- `samples/21-postgres/crud` → `http://localhost:8080/swagger-ui/index.html`
- `samples/21-postgres/batch` → `http://localhost:8080/swagger-ui/index.html`
- `samples/21-postgres/transactions` → `http://localhost:8080/swagger-ui/index.html`
- `samples/22-rabbitmq/direct` → `http://localhost:8080/swagger-ui/index.html`
- `samples/22-rabbitmq/fanout` → `http://localhost:8080/swagger-ui/index.html`
- `samples/22-rabbitmq/headers` → `http://localhost:8080/swagger-ui/index.html`
- `samples/22-rabbitmq/topic` → `http://localhost:8080/swagger-ui/index.html`
- `samples/23-redis/datastore` → `http://localhost:8080/swagger-ui/index.html`
- `samples/23-redis/cache-layer` → `http://localhost:8080/swagger-ui/index.html`
- `samples/23-redis/pubsub-events` → `http://localhost:8080/swagger-ui/index.html`
- `samples/25-websocket/server` → `http://localhost:8090/swagger-ui/index.html`
- `samples/26-traefik` → `http://localhost:8080/swagger-ui/index.html`
- `samples/27-async/basics` → `http://localhost:8080/swagger-ui/index.html`

Run each sample from its own folder:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Links:

1. [Useful links and resources](documentation/links.md)

## License:

[MIT License](LICENSE):
  - This project is licensed under the MIT License.

## Created by

Luciano Sampaio
