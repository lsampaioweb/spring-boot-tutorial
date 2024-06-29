Centralizing application configuration using Spring Boot Config Server simplifies the management of configuration properties across multiple environments and services. This guide will walk you through setting up a Config Server and configuring a Spring Boot application to retrieve its configuration from the server.

1. Server Setup.

    1. Add dependencies.

        Add the following dependencies to your `pom.xml` file:

        ```xml
        <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-config-server</artifactId>
          <version>4.1.2</version>
        </dependency>

        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
          <groupId>org.springframework.security</groupId>
          <artifactId>spring-security-config</artifactId>
        </dependency>
        ```

    1. Configure `application.yml`.

        ```yml
        spring:
          application:
            name: "cloud-config-server"
          cloud:
            config:
              server:
                git:
                  # Local repository.
                  uri: "file://${user.home}/git/datacenter/spring-boot/tutorial/samples/08-cloud-config/git-config"
                  cloneOnStart: true
                  # The name of the application and active profile.
                  search-paths: "{application}/{profile}"

                  # Remote repository example.
                  # uri: "https://github.com/{application}"

                  # Example for multiple repositories.
                  # repos:
                  #   client-01:
                  #     pattern: "cloud-config-client"
                  #     search-paths: "{application}/{profile}"
                  #     uri: "..."
                  #   client-02:
                  #     pattern: "client-02"
                  #     search-paths: "{application}/{profile}"
                  #     uri: "..."

        # Optional: Security configuration.
        security:
          user:
            name: "${USERNAME}"
            password: "${PASSWORD}"
        ```

    1. Enable Config Server.

        Add the `@EnableConfigServer` annotation to the main class:
        ```java
        ...
        import org.springframework.cloud.config.server.EnableConfigServer;

        @SpringBootApplication
        @EnableConfigServer
        public class ServerApplication {
          public static void main(String[] args) {
            SpringApplication.run(ServerApplication.class, args);
          }
        }
        ```

1. Create a Configuration Repository.

    Create a Git repository to store your configuration files. Create a folder for each project or Spring Boot application, and within each folder, create subfolders for each profile (e.g., `default`, `development`, `production`). Inside each subfolder, create an `application.yml` file with the application settings.

    default/application.yml
    ```yml
    user:
      role: "Default"

    server:
      port: 8080
    ```

    production/application.yml
    ```yml
    user:
      role: "Production"

    server:
      port: 8181
    ```

    Push these files to your Git repository, whether local or remote.

1. Client Setup.

    1. Add dependencies.

        Add the following dependency to your `pom.xml` file:

        ```xml
          <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
            <version>4.1.2</version>
          </dependency>
        ```

    1. Configure `application.yml`.

        ```yml
        spring:
          application:
            # The name of the application is the name of the folder from the Git repository.
            name: "cloud-config-client"
          profiles:
            # active: "default"
            active: "development"
            # active: "production"

          # Optional: Security configuration.
          # It must match the values from the server.
          cloud:
            config:
              username: "${USERNAME}"
              password: "${PASSWORD}"

          # Import configuration from the Config Server.
          config:
            # import: "configserver:http://localhost:8080"
            import: "configserver:https://jump-server-01.homelab:9443"
        ```

    1. Test the Configuration.

        Create a simple REST controller to read and print the configuration properties:

        ```java
        ...

        @RestController
        @RequestMapping("api/v1")
        @Slf4j
        public class helloController {

          @Value("${user.role}")
          private String role;

          @Value("${server.port}")
          private int port;

          @GetMapping("/hello")
          public String sayHello() {
            String message = String.format("Message: %s - %d", role, port);

            return message;
          }
        }
        ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
