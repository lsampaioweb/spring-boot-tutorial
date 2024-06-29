This guide will walk you through setting up a Spring Boot application with a `REST` controller that supports `GET`, `POST`, `PUT`, and `DELETE` operations.

1. Add Dependencies.

    Add the following dependencies to your `pom.xml` file:

    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    ```

1. Create the Model Class.

    Create a model class to represent the data structure you will be working with. For this example, let's use a `User` model with Lombok annotations to reduce boilerplate code:

    ```java
    ...

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class User {
      private Long id;
      private String name;
      private String email;
    }
    ```

1. Create the REST Controller.

    Create a REST controller with GET, POST, PUT, and DELETE endpoints, including some sample users:

    ```java
    ...

    @RestController
    @RequestMapping("/api/v1/users")
    public class UserController {

      private List<User> users = new ArrayList<>();

      public UserController() {
        users.add(new User(1L, "Zara", "zara@example.com"));
        users.add(new User(2L, "Malu", "malu@example.com"));
      }

      @GetMapping
      public List<User> getUsers() {
        return users;
      }

      @PostMapping
      public User createUser(@RequestBody User user) {
        users.add(user);

        return user;
      }

      @PutMapping("/{id}")
      public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

        if (user != null) {
          user.setName(userDetails.getName());
          user.setEmail(userDetails.getEmail());
        }

        return user;
      }

      @DeleteMapping("/{id}")
      public String deleteUser(@PathVariable Long id) {
        users.removeIf(u -> u.getId().equals(id));

        return String.format("User with ID '%d' has been deleted.", id);
      }

    }

1. Test the Endpoints.

    You can use tools like `Postman` or `curl` to test the endpoints:

    - GET /api/v1/users - Retrieve all users.

    ```bash
    curl -X GET http://localhost:8080/api/v1/users
    ```

    - POST /api/v1/users - Create a new user.
    ```bash
    curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"id":1,"name":"zara","email":"zara@example.com"}'
    ```

    - PUT /api/v1/users/{id} - Update an existing user.
    ```bash
    curl -X PUT http://localhost:8080/api/v1/users/1 -H "Content-Type: application/json" -d '{"name":"Malu","email":"malu@example.com"}'
    ```

    - DELETE /api/v1/users/{id} - Delete a user.
    ```bash
    curl -X DELETE http://localhost:8080/api/v1/users/1
    ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
