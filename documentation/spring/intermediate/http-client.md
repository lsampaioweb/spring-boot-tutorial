## HTTP Client

Spring Boot 3.1 and later versions include the new HTTP Client, which provides a more modern and flexible approach to making HTTP requests compared to the older RestTemplate. This guide will demonstrate how to use the new HTTP Client in a Spring Boot application.

1. Add HTTP Client Configuration.

    In your `application.yml`, you can configure settings related to the HTTP Client if necessary.

    ```yml
    # Because the API Rest application is running on 8080.
    server:
      port: 8081

    #spring:
    #  http:
    #    client:
    #      connect-timeout: 5000
    #      read-timeout: 5000

    external:
      api:
        base-url: "http://localhost:8080/api/v1"
        users: "${external.api.base-url}/users"
    ```

1. Create a Service Using the HTTP Client.

    Create a service class to use the new HTTP Client.

    ```java
    import jakarta.annotation.PostConstruct;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestClient;

    import java.util.List;

    @Service
    class UserService {

      private final RestClient.Builder restClientBuilder;
      private RestClient restClient;

      @Value("${external.api.users}")
      private String usersUrl;

      public UserService(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
      }

      @PostConstruct
      private void init() {
        this.restClient = restClientBuilder
            .baseUrl(usersUrl)
            .build();
      }

      public List<User> findAll() {
        return restClient
            .get()
            .retrieve()
            .body(new ParameterizedTypeReference<List<User>>() {});
      }

      public User findById(Integer id) {
        return restClient
            .get()
            .uri("/{id}", id)
            .retrieve()
            .body(User.class);
      }

      public User create(User user) {
        return restClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(user)
            .retrieve()
            .body(User.class);
      }

      public User update(Integer id, User user) {
        return restClient
            .put()
            .uri("/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(user)
            .retrieve()
            .body(User.class);
      }

      public ResponseEntity<Void> delete(Integer id) {
        return restClient
            .delete()
            .uri("/{id}", id)
            .retrieve()
            .toBodilessEntity();
      }
    }
    ```

1. Create a Controller to Use the Service.

    Create a controller class to expose an endpoint that uses the service.

    ```java
    import org.springframework.http.HttpStatusCode;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("http/v1/users")
    public class UserController {

      private final UserService userService;

      public UserController(UserService userService) {
        this.userService = userService;
      }

      @GetMapping("")
      List<User> findAll() {
        return userService.findAll();
      }

      @GetMapping("/{id}")
      User findById(@PathVariable Integer id) {
        return userService.findById(id);
      }

      @PostMapping
      User create(@RequestBody User user) {
        return userService.create(user);
      }

      @PutMapping("/{id}")
      User update(@PathVariable Integer id, @RequestBody User user) {
        return userService.update(id, user);
      }

      @DeleteMapping("/{id}")
      ResponseEntity<Void> delete(@PathVariable Integer id) {
        HttpStatusCode status = userService.delete(id).getStatusCode();

        if ((status != null) && (status.is2xxSuccessful())) {
          return ResponseEntity.ok().build();
        } else {
          return ResponseEntity.notFound().build();
        }
      }
    }
    ```

1. Create a User model class.

    Create a model class to have the user's attribute.

    ```java
    import lombok.Data;

    @Data
    public class User {
      private int id;
      private String name;
      private String email;
    }
    ```

1. Testing the HTTP Client

    1. Start your Spring Boot API Rest application.
    1. Start your Spring Boot HTTP Client application.

        Get all users:
        ```bash
        curl -X GET http://localhost:8081/http/v1/users
        ```

        Get one user by its id:
        ```bash
        curl -X GET http://localhost:8081/http/v1/users/1
        ```

        Create a user:
        ```bash
        curl -X POST http://localhost:8081/http/v1/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john.doe@example.com"}'
        ```

        Update a user:
        ```bash
        curl -X PUT http://localhost:8081/http/v1/users/3 -H "Content-Type: application/json" -d '{"name":"Jane Doe","email":"jane.doe@example.com"}'
        ```

        Delete a user:
        ```bash
        curl -X DELETE http://localhost:8081/http/v1/users/3
        ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
