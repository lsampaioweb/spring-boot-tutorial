Spring Boot Actuator provides production-ready features to help you monitor and manage your Spring Boot application. This guide will walk you through setting up a Spring Boot application with Actuator and exploring its main features.

1. Add Dependencies.

    Add the following dependencies to your `pom.xml` file:
    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    ```

    If you want to have a username and password for the Actuator endpoints, add the following dependency:
    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ```

1. Configure `application.yml`.

    Spring Boot Actuator provides several built-in endpoints that allow you to monitor and manage your application. By default, these endpoints are available under the `/actuator` path.

    1. Some commonly used endpoints include:

        - `/actuator/health` - Shows application health information.
        - `/actuator/info` - Displays arbitrary application information.
        - `/actuator/metrics` - Shows metrics information.
        - `/actuator/env` - Displays environment properties.
        - `/actuator/loggers` - Shows and configures loggers in the application.

    1. Explanation of `show-details` Property:

        The `management.endpoint.health.show-details` property in the `application.yml` file configures the level of detail shown in the /actuator/health endpoint. This property can take the following values:

        `never`: The details are never shown, even if the request is authenticated.

        `when-authorized`: The details are shown only when the request is authenticated and the user has the appropriate roles.

        `always`: The details are always shown, regardless of authentication.

        In this example, setting show-details: "always" means that the health details will always be shown in the response from the `/actuator/health` endpoint. This can be useful during development and testing to get detailed information about the health of various components in your application. However, be cautious about using this setting in a production environment, as it might expose sensitive information.

        ```yml
        management:
          endpoints:
            web:
              exposure:
                # include: "*"
                include: "health,metrics"

          endpoint:
            health:
              show-details: "always"

        # Add if you want to have a username and password for the actuator endpoint.
        spring:
          security:
            user:
              name: "${USERNAME}"
              password: "${PASSWORD}"
        ```

1. Test the Configuration.

    After configuring your application, you can test the Actuator endpoints:

    Local machine:
      - http://localhost:8080/actuator

    Remote server:
      - http://jump-server-01.homelab:8080/actuator
      - http://jump-server-01.homelab:8080/actuator/health
      - http://jump-server-01.homelab:8080/actuator/metrics

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
