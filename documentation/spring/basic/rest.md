This guide walks through a versioned REST API with pagination, sorting, and OpenAPI.

Working sample: `samples/08-restapi`

With the `development` profile, Swagger UI is at `http://localhost:8080/swagger-ui/index.html`. Production disables it.

1. Add dependencies.

    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.data</groupId>
      <artifactId>spring-data-commons</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>${springdoc.version}</version>
    </dependency>
    ```

    Validation is the next lesson (`samples/09-validation`). This sample does not add `spring-boot-starter-validation`.

1. Enable OpenAPI in development.

    `application-development.yml`:

    ```yml
    springdoc:
      swagger-ui:
        enabled: true
    ```

    `application-production.yml` sets `springdoc.swagger-ui.enabled: false`.

    An `OpenAPI` bean can resolve title and description from `MessageSource`.

1. Use records for the model and DTOs.

    ```java
    public record User(Long id, String name, String email) {
    }

    public record UserRequest(String name, String email) {
    }

    public record UserResponse(Long id, String name, String email) {
    }
    ```

1. Keep HTTP types out of the service.

    `UserService` returns `Page<UserResponse>` and `UserResponse`. Pagination uses Spring Data `Pageable`. Invalid `sort` properties are resolved through `MessageSource`.

    The in-memory implementation lives in `UserServiceImpl`. A small `UserMapper` copies domain records to response DTOs. MapStruct is introduced later in `samples/11-mapstruct`.

1. Expose `/api/v1/users`.

    ```java
    @RestController
    @RequestMapping("/api/v1/users")
    class UserRestController {

      private final UserService userService;

      @GetMapping
      public ResponseEntity<Page<UserResponse>> findAll(
          @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
      }

      @PostMapping
      public ResponseEntity<UserResponse> create(@RequestBody UserRequest request,
          UriComponentsBuilder uriBuilder) {
        UserResponse createdUser = userService.create(request);
        URI location = uriBuilder.path("/{id}").buildAndExpand(createdUser.id()).toUri();

        return ResponseEntity.created(location).body(createdUser);
      }
    }
    ```

    `GET /{id}`, `PUT /{id}`, and `DELETE /{id}` follow the same pattern. Missing users return `404`.

1. Resolve HTTP locale from `Accept-Language`.

    See `samples/08-restapi` `I18nLocaleResolverConfig` and the i18n guide. Sort error messages use `LocaleContextHolder`.

1. Test the endpoints.

    Start the sample, then:

    ```bash
    curl -X GET http://localhost:8080/api/v1/users
    curl -X GET "http://localhost:8080/api/v1/users?page=1&size=3&sort=name,asc"
    curl -X GET http://localhost:8080/api/v1/users/1
    curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" \
      -d '{"name":"John Doe","email":"john.doe@example.com"}'
    curl -X PUT http://localhost:8080/api/v1/users/11 -H "Content-Type: application/json" \
      -d '{"name":"Jane Doe","email":"jane.doe@example.com"}'
    curl -X DELETE http://localhost:8080/api/v1/users/11
    curl -X GET http://localhost:8080/v3/api-docs
    ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
