## HTTP Client

Spring Boot 3.1 and later versions include the new HTTP Client, which provides a more modern and flexible approach to making HTTP requests compared to the older RestTemplate. This guide will demonstrate how to use the new HTTP Client in a Spring Boot application.

1. Add Dependencies.

    Add the following dependencies to your `pom.xml` file:

    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-hateoas</artifactId>
    </dependency>
    ```

1. Exclude the Auto Configuration of a Datasource:

    Because we are not using a database, add the following to your `application.yml` file:

    ```yml
    spring:
      autoconfigure:
        exclude: "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
    ```

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

1. Create the Model Class.

    Create a model class to represent the data structure you will be working with. For this example, let's use a `User` model with Lombok annotations to reduce boilerplate code:

    ```java
    ...

    @Data
    public class User {
      private int id;
      private String name;
      private String email;
    }
    ```

1. Create the Service class.

    Create the service class to handle the business logic:

    ```java
    ...
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

      PagedModel<EntityModel<User>> findAll(Pageable pageable) {
        return restClient
            .get()
            .uri(getPagingAndSortingUrl(pageable))
            .retrieve()
            .body(new ParameterizedTypeReference<PagedModel<EntityModel<User>>>() {
            });
      }

      Optional<User> findById(Integer id) {
        User user = restClient
            .get()
            .uri("/{id}", id)
            .retrieve()
            .body(User.class);

        return Optional.ofNullable(user);
      }

      User create(User user) {
        return restClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(user)
            .retrieve()
            .body(User.class);
      }

      Optional<User> update(Integer id, User user) {
        User updatedUser = restClient
            .put()
            .uri("/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(user)
            .retrieve()
            .body(User.class);

        return Optional.ofNullable(updatedUser);
      }

      boolean delete(Integer id) {
        HttpStatusCode status = restClient
            .delete()
            .uri("/{id}", id)
            .retrieve()
            .toBodilessEntity()
            .getStatusCode();

        return ((status == HttpStatus.OK) || (status == HttpStatus.NO_CONTENT));
      }

      private String getPagingAndSortingUrl(Pageable pageable) {
        return UriComponentsBuilder.fromHttpUrl(usersUrl)
            .queryParam("page", pageable.getPageNumber())
            .queryParam("size", pageable.getPageSize())
            .queryParam("sort", formatSort(pageable.getSort()))
            .toUriString();
      }

      private String formatSort(Sort sort) {
        return sort.stream()
            .map(order -> order.getProperty() + "," + order.getDirection())
            .collect(Collectors.joining(","));
      }
    }
    ```

1. Create the REST Controller.

    Create a REST controller with GET, POST, PUT, and DELETE endpoints, including some sample users:

    ```java
    ...
    @RestController
    @RequestMapping("http/v1/users")
    public class UserController {

      private final UserService userService;

      public UserController(UserService userService) {
        this.userService = userService;
      }

      @GetMapping
      public ResponseEntity<PagedModel<EntityModel<User>>> findAll(Pageable pageable) {
        PagedModel<EntityModel<User>> users = userService.findAll(pageable);

        return ResponseEntity.ok(users);
      }

      @GetMapping("/{id}")
      public ResponseEntity<User> findById(@PathVariable Integer id) {
        Optional<User> user = userService.findById(id);

        return user.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
      }

      @PostMapping
      public ResponseEntity<User> create(@RequestBody User user, UriComponentsBuilder uriBuilder) {
        User createdUser = userService.create(user);

        URI location = uriBuilder.path("/{id}").buildAndExpand(createdUser.getId()).toUri();

        return ResponseEntity.created(location).body(createdUser);
      }

      @PutMapping("/{id}")
      public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> updatedUser = userService.update(id, user);

        return updatedUser.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean userRemoved = userService.delete(id);

        if (userRemoved) {
          return ResponseEntity.ok().build();
        } else {
          return ResponseEntity.notFound().build();
        }
      }
    }
    ```

1. Test the Endpoints.

    You can use tools like `Postman` or `curl` to test the endpoints:

    1. Start your Spring Boot API Rest application.
    1. Start your Spring Boot HTTP Client application.

    - Get all users.
      ```bash
      curl -X GET http://localhost:8081/http/v1/users
      ```

    - Get paginated and sorted users.
      ```bash
      curl -X GET "http://localhost:8081/http/v1/users?page=1&size=3&sort=name,asc"
      ```

    - Get a user by ID.
      ```bash
      curl -X GET http://localhost:8081/http/v1/users/1
      ```

    - Create a user.
      ```bash
      curl -X POST http://localhost:8081/http/v1/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john.doe@example.com"}'
      ```

    - Update a user.
      ```bash
      curl -X PUT http://localhost:8081/http/v1/users/11 -H "Content-Type: application/json" -d '{"name":"Jane Doe","email":"jane.doe@example.com"}'
      ```

    - Delete a user.
      ```bash
      curl -X DELETE http://localhost:8081/http/v1/users/11
      ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
