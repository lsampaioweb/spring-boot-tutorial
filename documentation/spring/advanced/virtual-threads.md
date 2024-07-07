## Virtual Threads

Virtual Threads are a lightweight concurrency mechanism introduced in Java as part of Project Loom. They enable a more scalable and efficient way to handle large numbers of concurrent tasks without the overhead associated with traditional threads. This guide demonstrates how to use Virtual Threads in a Spring Boot application.

### Prerequisites

1. Java 21:

    Ensure your project is using `JDK 21` or later, as Virtual Threads are fully supported in this version.

### Steps to Implement Virtual Threads in Spring Boot

1. Configure Virtual Threads in `application.yml`.

    Enable Virtual Threads in your Spring Boot application by adding the following configuration to your `application.yml` file:

    ```yaml
    spring:
      threads:
        virtual:
          # To enable it.
          enabled: true
          # To disable it.
          enabled: false
    ```

1. Create the Controller.

    Implement a controller to demonstrate the use of Virtual Threads. Create a file named `HttpBinController.java`.

    ```java
    ...
    @RestController
    @RequestMapping("/httpbin")
    @Slf4j
    public class HttpBinController {

      private static final String HTTPBIN_BASE_URL = "https://httpbin.org/";

      private final RestClient restClient;

      public HttpBinController(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder.baseUrl(HTTPBIN_BASE_URL).build();
      }

      @GetMapping("/block/{seconds}")
      public String delay(@PathVariable int seconds) {
        ResponseEntity<Void> result = restClient
            .get()
            .uri("/delay/" + seconds)
            .retrieve()
            .toBodilessEntity();

        String message = String.format("%d on %s", result.getStatusCode().value(), Thread.currentThread());

        log.info(message);

        return message;
      }
    }
    ```

1. Testing Virtual Threads.

    1. Start your Spring Boot application.
    1. Navigate to http://localhost:8080/httpbin/block/3.
    1. Change the value of `spring:threads:virtual:enabled:` from `true` to `false` and test it again.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
