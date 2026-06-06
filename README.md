# Java Libraries and Spring Boot Projects

Documentation and working samples for building Java libraries and Spring Boot projects following modern architecture and best practices.

**Project Specifications:**
- **Spring Boot:** 3.5.14
- **Java:** 25
- **Maven:** 3.9+
- **Status:** All 19 samples compile and pass tests ✓

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

### Working Samples:

This project includes 19 runnable samples demonstrating Spring Boot features and best practices. All samples follow standardized architecture conventions and are validated via Maven compilation and test execution.

**Sample Index:**

1. **01-pom** – Maven project structure and configuration
2. **02-devtools** – Spring Boot DevTools for development workflow
3. **03-logs** – Logging configuration and management
4. **04-lombok** – Lombok integration for code generation
5. **05-profiles** – Spring Boot profiles for environment-specific configuration
6. **06-actuator** – Application monitoring and health checks
7. **07-thymeleaf** – Server-side templating with Thymeleaf
8. **08-cloud-config** – Externalized configuration with Spring Cloud Config
9. **09-https** – HTTPS/SSL configuration and setup
10. **10-i18n** – Internationalization (i18n) with message bundles
11. **11-restapi** – RESTful API design with DTOs and validation
12. **12-http-client** – HTTP client calls to external services
13. **13-k6** – Performance and load testing
14. **14-virtual-threads** – Virtual threads for asynchronous processing
15. **15-exception-handling** – Centralized exception handling and error responses
16. **16-container** – Docker containerization and Compose orchestration
17. **17-traefik** – Reverse proxy integration with Traefik
18. **18-rabbitmq** – Async messaging with RabbitMQ
19. **19-postgres** – Database integration with PostgreSQL and JDBC

**To run a specific sample:**
```bash
cd samples/<sample-name>
mvn clean test  # Run tests
mvn clean spring-boot:run  # Run the application
```

**To run all samples:**
```bash
for dir in samples/[0-9]*; do
  [ -f "$dir/pom.xml" ] && mvn -f "$dir/pom.xml" clean test
done
```

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
    - Integrating Traefik as a reverse proxy.
1. [Vault](documentation/spring/integrations/vault.md)
    - Using HashiCorp Vault for secrets management.
1. [PostgreSQL](documentation/spring/integrations/postgresql.md)
    - Integrating PostgreSQL database.
1. [Redis](documentation/spring/integrations/redis.md)
    - Using Redis for caching and data storage.
1. [RabbitMQ](documentation/spring/integrations/rabbitmq.md)
    - Messaging with RabbitMQ.

## Links:

1. [Useful links and resources](documentation/links.md)

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
- **Testing:** Slice tests (`@WebMvcTest`, `@DataJdbcTest`) + integration tests

For detailed conventions, see the instruction files in: `https://github.com/lsampaioweb/ia-instructions`

## License:

[MIT License](LICENSE):
  - This project is licensed under the MIT License.

## Created by:

1. Luciano Sampaio
