## HTTP Client

Working sample: `samples/12-http-client`. Point `external.api.users` at a running `samples/08-restapi` instance (`http://localhost:8080/api/v1/users`).

Spring Boot 3.1 and later include `RestClient`. Use a named bean with connect and read timeouts. Call it from a repository, not from a controller.

1. Add Dependencies.

    Add the following dependencies to your `pom.xml` file:

    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.data</groupId>
      <artifactId>spring-data-commons</artifactId>
    </dependency>
    ```

    `spring-data-commons` is only for `Pageable` / `Page`. Do not add JPA or HATEOAS for this client.

1. Exclude the Auto Configuration of a Datasource.

    Because we are not using a database, add the following to your `application.yml` file:

    ```yml
    spring:
      autoconfigure:
        exclude: "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
    ```

1. Add HTTP Client Configuration.

    Run this app on `8081` in development so it can sit next to `08-restapi` on `8080`.

    ```yml
    external:
      api:
        base-url: "http://localhost:8080/api/v1"
        users: "${external.api.base-url}/users"
        connect-timeout: "2s"
        read-timeout: "5s"
    ```

    Bind those values to a `@ConfigurationProperties` record and build a named `RestClient` bean:

    ```java
    @ConfigurationProperties(prefix = "external.api")
    public record ExternalApiProperties(
        String users,
        Duration connectTimeout,
        Duration readTimeout) {
    }

    @Configuration
    @EnableConfigurationProperties(ExternalApiProperties.class)
    public class HttpClientConfiguration {

      @Bean
      RestClient usersRestClient(ExternalApiProperties properties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.connectTimeout());
        requestFactory.setReadTimeout(properties.readTimeout());

        return RestClient.builder()
            .requestFactory(requestFactory)
            .baseUrl(properties.users())
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
      }
    }
    ```

1. Create the Model Class.

    Use a Java record. The remote JSON is Spring `Page`, so the repository deserializes into a small `PageImpl` subtype.

    ```java
    public record User(Long id, String name, String email) {
    }
    ```

1. Create the Repository class.

    Keep `RestClient` in the repository. Forward `page`, `size`, and `sort` as query parameters.

    ```java
    @Repository
    class UserRepositoryImpl implements UserRepository {

      private static final String ID_PATH = "/{id}";

      private final RestClient usersRestClient;

      UserRepositoryImpl(RestClient usersRestClient) {
        this.usersRestClient = usersRestClient;
      }

      @Override
      public Page<User> findAll(Pageable pageable) {
        RestPage<User> page = usersRestClient
            .get()
            .uri(uriBuilder -> {
              uriBuilder.queryParam("page", pageable.getPageNumber())
                  .queryParam("size", pageable.getPageSize());
              pageable.getSort().forEach(order -> uriBuilder.queryParam("sort",
                  order.getProperty() + "," + order.getDirection().name().toLowerCase(Locale.ROOT)));
              return uriBuilder.build();
            })
            .retrieve()
            .body(new ParameterizedTypeReference<RestPage<User>>() {
            });

        if (page == null) {
          return Page.empty(pageable);
        }

        return page;
      }

      @Override
      public Optional<User> findById(Long id) {
        return usersRestClient
            .get()
            .uri(ID_PATH, id)
            .exchange((request, response) -> {
              if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
              }

              return Optional.ofNullable(response.bodyTo(User.class));
            });
      }

      @Override
      public User create(User user) {
        return usersRestClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(user)
            .retrieve()
            .body(User.class);
      }
    }
    ```

    The service maps `User` to `UserResponse` with MapStruct and does not call `RestClient` itself. Create mappings use `@Mapping(target = "id", constant = "0L")` because `User.id` is a `Long`.

1. Create the REST Controller.

    Expose the same `/api/v1/users` contract as the upstream API.

    ```java
    @RestController
    @RequestMapping("/api/v1/users")
    @RequiredArgsConstructor
    @Tag(name = "{openapi.users.tag}")
    class UserRestController {

      private final UserService userService;

      @GetMapping
      @Operation(summary = "{openapi.users.findAll.summary}")
      public ResponseEntity<Page<UserResponse>> findAll(
          @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
      }

      @GetMapping("/{id}")
      @Operation(summary = "{openapi.users.findById.summary}")
      public ResponseEntity<UserResponse> findById(@PathVariable @Positive Long id) {
        Optional<UserResponse> user = userService.findById(id);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
      }

      @PostMapping
      @Operation(summary = "{openapi.users.create.summary}")
      public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request,
          UriComponentsBuilder uriBuilder) {
        UserResponse createdUser = userService.create(request);
        URI location = uriBuilder.path("/{id}").buildAndExpand(createdUser.id()).toUri();

        return ResponseEntity.created(location).body(createdUser);
      }

      @PutMapping("/{id}")
      @Operation(summary = "{openapi.users.update.summary}")
      public ResponseEntity<UserResponse> update(@PathVariable @Positive Long id,
          @Valid @RequestBody UserRequest request) {
        Optional<UserResponse> updatedUser = userService.update(id, request);

        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
      }

      @DeleteMapping("/{id}")
      @Operation(summary = "{openapi.users.delete.summary}")
      public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        if (userService.delete(id)) {
          return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
      }
    }
    ```

1. Test the Endpoints.

    You can use tools like `Postman` or `curl` to test the endpoints:

    1. Start your Spring Boot API Rest application (`samples/08-restapi` on port `8080`).
    1. Start your Spring Boot HTTP Client application (`samples/12-http-client` on port `8081`).

    - Get all users.
      ```bash
      curl -X GET http://localhost:8081/api/v1/users
      ```

    - Get paginated and sorted users.
      ```bash
      curl -X GET "http://localhost:8081/api/v1/users?page=1&size=3&sort=name,asc"
      ```

    - Get a user by ID.
      ```bash
      curl -X GET http://localhost:8081/api/v1/users/1
      ```

    - Create a user.
      ```bash
      curl -X POST http://localhost:8081/api/v1/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john.doe@example.com"}'
      ```

    - Update a user.
      ```bash
      curl -X PUT http://localhost:8081/api/v1/users/11 -H "Content-Type: application/json" -d '{"name":"Jane Doe","email":"jane.doe@example.com"}'
      ```

    - Delete a user.
      ```bash
      curl -X DELETE http://localhost:8081/api/v1/users/11
      ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
