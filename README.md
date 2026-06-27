# Java Libraries and Spring Boot Projects

Documentation and working samples for building Java libraries and Spring Boot projects following modern architecture and best practices.

**Project Specifications:**
- **Spring Boot:** 4.1.0
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
    - Common Maven commands and usage.
1. [Upgrade Process](documentation/maven/upgrade.md):
    - Practical workflow to keep all sample POMs updated.
1. [DevTools](documentation/spring/basic/devtools.md):
    - Enabling and using Spring Boot DevTools.
1. [Logs](documentation/spring/basic/logs.md):
    - Configuring and managing logs in Spring Boot.
1. [Lombok](documentation/spring/basic/lombok.md)
    - Integrating Lombok into your Spring Boot project.
1. [Profile](documentation/spring/basic/profile.md)
    - Using Spring Boot profiles.
1. [Actuator](documentation/spring/basic/actuator.md)
    - Monitoring and managing your Spring Boot application.

### Spring Boot Intermediate:
1. [i18n](documentation/spring/intermediate/i18n.md)
    - Implementing internationalization (i18n) in Spring Boot.
1. [REST](documentation/spring/basic/rest.md)
    - Creating RESTful web services.
    - Handle pagination.
    - Handle sorting.
1. [Thymeleaf](documentation/spring/basic/thymeleaf.md)
    - Using Thymeleaf as the templating engine.
1. [Validation](documentation/spring/intermediate/validation.md)
    - Input validation for REST APIs and web forms.
1. [Exception Handling](documentation/spring/intermediate/exception-handling.md)
    - Handling exceptions in Spring Boot applications.
1. [HTTPS](documentation/spring/intermediate/https.md)
    - Securing your application with HTTPS.
1. [HTTP Client](documentation/spring/intermediate/http-client.md)
    - Making HTTP requests using the new HTTP Client in Spring Boot.
    - Handle pagination.
    - Handle sorting.

### Spring Boot Advanced:
1. [Cloud Config](documentation/spring/advanced/cloud-config.md)
    - Externalized configuration using Spring Cloud Config.
1. [Virtual Threads](documentation/spring/advanced/virtual-threads.md)
    - Using virtual threads in Spring Boot.
1. [WebSocket](documentation/spring/advanced/websocket.md)
    - Implementing WebSocket communication.
1. [Security](documentation/spring/advanced/security.md)
    - Securing your Spring Boot application.
1. [Container](documentation/spring/extra/container.md)
    - Containerizing your Spring Boot application with Docker.
1. [K6](documentation/spring/tests/k6.md)
    - Performance testing with K6.

### Spring Boot Integrations:
1. [Traefik](documentation/spring/integrations/traefik.md)
    - Integrating Traefik as a reverse proxy for container routing.
1. [Vault](documentation/spring/integrations/vault.md)
    - Reading secrets from HashiCorp Vault with startup caching.
1. [PostgreSQL](documentation/spring/integrations/postgresql.md)
    - Integrating PostgreSQL database via JdbcClient.
1. [Redis](documentation/spring/integrations/redis.md)
    - Using Redis for caching and data storage.
1. [RabbitMQ](documentation/spring/integrations/rabbitmq.md)
    - Messaging with RabbitMQ (direct, fanout, topic, headers exchanges).

## Infrastructure Services

All integration samples use containerized infrastructure defined in `samples/infrastructure/`:
- **Traefik** — Reverse proxy for HTTP routing and load balancing
- **Vault** — HashiCorp Vault for secrets management
- **PostgreSQL** — Relational database
- **Redis** — In-memory data store
- **RabbitMQ** — Message broker

Each service runs in Podman Compose with security hardening (read-only filesystems, dropped capabilities, no-new-privileges).

To use any integration:
1. Start infrastructure: `cd samples/infrastructure/{service} && podman compose up -d`
2. Configure `.env` for your sample (copy from `.env.example`)
3. Run the sample: `mvn spring-boot:run`
4. Stop infrastructure: `podman compose down`

See individual integration documentation for detailed setup steps.

### Swagger UI (Development Profile)
The following samples expose Swagger UI when running with the `development` profile:

- `samples/08-cloud-config/client` → `http://localhost:8080/swagger-ui/index.html`
- `samples/10-i18n` → `http://localhost:8080/swagger-ui/index.html`
- `samples/11-restapi` → `http://localhost:8080/swagger-ui/index.html`
- `samples/12-http-client` → `http://localhost:8080/swagger-ui/index.html`
- `samples/14-virtual-threads` → `http://localhost:8080/swagger-ui/index.html`
- `samples/15-exception-handling` → `http://localhost:8080/swagger-ui/index.html`
- `samples/16-container` → `http://localhost:8080/swagger-ui/index.html`
- `samples/17-traefik` → `http://localhost:8080/swagger-ui/index.html`
- `samples/18-rabbitmq/direct` → `http://localhost:8080/swagger-ui/index.html`
- `samples/18-rabbitmq/fanout` → `http://localhost:8080/swagger-ui/index.html`
- `samples/18-rabbitmq/headers` → `http://localhost:8080/swagger-ui/index.html`
- `samples/18-rabbitmq/topic` → `http://localhost:8080/swagger-ui/index.html`
- `samples/19-postgres` → `http://localhost:8080/swagger-ui/index.html`
- `samples/20-websocket/server` → `http://localhost:8090/swagger-ui/index.html`

Run each sample from its own folder:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Tutorial TODO Projects:

### Missing sample applications:
1. Redis sample application (using Spring Data Redis)
    - Status: MISSING
    - Next action: Create a new sample folder and implementation for Redis caching integration (suggested path: samples/22-redis).

### Placeholder documentation pages:
1. [documentation/spring/advanced/security.md](documentation/spring/advanced/security.md)
    - Status: PLACEHOLDER
    - Next action: Replace XXX content with a complete security tutorial.

### Maintenance:
1. Update this section whenever a missing sample is created, placeholder documentation is completed, or sample numbering/topic mapping changes.
1. Keep status values standardized: MISSING, PLACEHOLDER, NON-APP (EXPECTED).

## Links:

1. [Useful links and resources](documentation/links.md)

## License:

[MIT License](LICENSE):
  - This project is licensed under the MIT License.

## Created by:

1. Luciano Sampaio
