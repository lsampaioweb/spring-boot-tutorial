Spring Boot Actuator provides production-ready endpoints to monitor and manage an application.

Working sample: `samples/07-actuator`

This lesson adds Actuator over HTTP. `spring-boot-starter-web` is included only so `/actuator/*` is reachable in a browser or with curl. REST controllers are the next lesson. Do not add Spring Security here; that is `samples/18-security`.

1. Add dependencies.

    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    ```

1. Expose a small set of endpoints.

    `application.yml` in the sample:

    ```yml
    management:
      endpoints:
        web:
          exposure:
            include: "health,info,metrics"
      endpoint:
        health:
          probes:
            enabled: true
          show-details: "when-authorized"
    ```

    Common endpoints:

    - `/actuator/health` — application health (and liveness/readiness when probes are enabled)
    - `/actuator/info` — application information
    - `/actuator/metrics` — metrics

    `management.endpoint.health.show-details`:

    - `never` — never include component details
    - `when-authorized` — details only for an authorized caller
    - `always` — always include details (fine for local debugging, noisy or sensitive in production)

    The development profile in this sample can set `show-details: "always"`. Production keeps details hidden.

    Do not expose `include: "*"`. Leave sensitive endpoints (`env`, `beans`, `heapdump`, and similar) off unless you need them and protect them. Spring Security for Actuator comes later (`samples/18-security`, `samples/26-traefik`).

1. Test the endpoints.

    ```bash
    cd samples/07-actuator
    mvn spring-boot:run -Dspring-boot.run.profiles=development
    ```

    Then open `http://localhost:8080/actuator` and `http://localhost:8080/actuator/health`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
