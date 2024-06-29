Spring Boot Actuator provides production-ready features to help you monitor and manage your Spring Boot application. This guide will walk you through setting up a Spring Boot application with Actuator and exploring its main features.

1. Add Dependencies.

    Add the following dependencies to your `pom.xml` file:
    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    ```

    Add if you want to have a username and password for the actuator endpoint.
    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ```

1. Configure `application.yml`.

    Spring Boot Actuator provides several built-in endpoints that allow you to monitor and manage your application. By default, these endpoints are available under the `/actuator` path. Some commonly used endpoints include:

    - `/actuator/health` - Shows application health information.
    - `/actuator/info` - Displays arbitrary application information.
    - `/actuator/metrics` - Shows metrics information.
    - `/actuator/env` - Displays environment properties.
    - `/actuator/loggers` - Shows and configures loggers in the application.

    ```yml
    management:
      endpoints:
        web:
          exposure:
            include: "*"
            # include: "health,metrics"

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

    Local machine:
      - http://localhost:8080/actuator

    Remote server:
      - http://jump-server-01.homelab:8080/actuator
      - http://jump-server-01.homelab:8080/actuator/health

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
