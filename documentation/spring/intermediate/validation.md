<!-- filepath: documentation/spring/intermediate/validation.md -->

## Validation

This guide shows how to validate REST payloads using `spring-boot-starter-validation` and `@Valid`.

1. Add dependency.

    Add the validation starter to your `pom.xml`:

    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    ```

1. Define request constraints.

    Add Jakarta Bean Validation annotations to the request DTO.

    ```java
    package com.learning.restapi.user;

    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;

    public record UserRequest(
        @NotBlank String name,
        @Email @NotBlank String email) {
    }
    ```

1. Trigger validation in controller methods.

    Apply `@Valid` on `@RequestBody` parameters:

    ```java
    @PostMapping
    public ResponseEntity<UserResponse> create(
        @Valid @RequestBody UserRequest request,
        UriComponentsBuilder uriBuilder) {
      ...
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody UserRequest request) {
      ...
    }
    ```

1. Test invalid payloads.

    Send invalid data to confirm validation is active:

    ```bash
    curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"name":"","email":"invalid-email"}'
    ```

    Expected behavior: API returns `400 Bad Request` when constraints are violated.

[Go Back](../../../README.md)

#
### Created by:
1. Luciano Sampaio.
