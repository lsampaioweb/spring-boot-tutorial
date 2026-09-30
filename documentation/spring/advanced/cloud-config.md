Centralizing application configuration using Spring Boot Config Server simplifies the management of configuration properties across multiple environments and services. This guide will walk you through setting up a Config Server and configuring a Spring Boot application to retrieve its configuration from the server.

1. Server Setup.

    1. Add dependencies.

        Manage Spring Cloud dependencies with BOM and add the Config Server starter.

        In your `pom.xml`, declare the Spring Cloud BOM in `dependencyManagement`:

        ```xml
        <properties>
          <spring-cloud.version>2025.1.3</spring-cloud.version>
        </properties>

        <dependencyManagement>
          <dependencies>
            <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-dependencies</artifactId>
              <version>${spring-cloud.version}</version>
              <type>pom</type>
              <scope>import</scope>
            </dependency>
          </dependencies>
        </dependencyManagement>
        ```

        Then add the following dependencies:

        ```xml
        <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-config-server</artifactId>
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

      For portability, run the server from `samples/19-cloud-config/server` or set `CONFIG_REPO_PATH` to the `git-config` folder:

      ```bash
      export CONFIG_REPO_PATH="$PWD/samples/19-cloud-config/git-config"
      ```

        ```yml
        spring:
          application:
            name: "cloud-config-server"
          cloud:
            config:
              server:
                git:
                  # Local repository. Default works when Maven runs from samples/19-cloud-config/server.
                  uri: "file://${CONFIG_REPO_PATH:${user.dir}/../git-config}"
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

    cloud-config-client/default/application.yml
    ```yml
    user:
      role: "Default"

    server:
      port: 8080
    ```

    cloud-config-client/development/application.yml
    ```yml
    user:
      role: "development"

    server:
      port: 8181
    ```

    Push these files to your Git repository, whether local or remote.

1. Client Setup.

    1. Add dependencies.

        Add the following dependency to your `pom.xml` file (version comes from the Spring Cloud BOM):

        ```xml
          <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
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
            # import: "optional:configserver:https://config-server.example:9443"
            import: "optional:configserver:https://localhost:9443"
        ```

    1. Test the Configuration.

        Create a simple REST controller to read and print the configuration properties:

        ```java
        ...

        @RestController
        @RequestMapping("api/v1")
        @Slf4j
        public class HelloRestController {

          private final HelloConfigurationProperties properties;

          public HelloRestController(HelloConfigurationProperties properties) {
            this.properties = properties;
          }

          @GetMapping("/hello")
          public ResponseEntity<HelloResponse> sayHello() {
            String message = String.format("Message: %s - %d", properties.role(), properties.serverPort());

            return ResponseEntity.ok(new HelloResponse(message));
          }
        }
        ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
