This guide will walk you through setting up a Spring Boot application with a `REST` controller that supports `GET`, `POST`, `PUT`, and `DELETE` operations.

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
    ```

1. Exclude the Auto Configuration of a Datasource:

    Because we are not using a database, add the following to your `application.yml` file:

    ```yml
    spring:
      autoconfigure:
        exclude: "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
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

      private List<User> users = new ArrayList<>();

      public UserService() {
        users.add(new User(1, "user-01", "user-01@example.com"));
        users.add(new User(2, "user-02", "user-02@example.com"));
        users.add(new User(3, "user-03", "user-03@example.com"));
        users.add(new User(4, "user-04", "user-04@example.com"));
        users.add(new User(5, "user-05", "user-05@example.com"));
        users.add(new User(6, "user-06", "user-06@example.com"));
        users.add(new User(7, "user-07", "user-07@example.com"));
        users.add(new User(8, "user-08", "user-08@example.com"));
        users.add(new User(9, "user-09", "user-09@example.com"));
        users.add(new User(10, "user-10", "user-10@example.com"));
      }

      List<User> findAll() {
        return users;
      }

      Page<User> paginateAndSort(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Sort sort = pageable.getSort();

        List<User> paginatedUsers = getPaginatedList(users, pageNumber, pageSize);
        List<User> sortedPaginatedUsers = getSortedUsers(paginatedUsers, sort);

        return new PageImpl<>(sortedPaginatedUsers, PageRequest.of(pageNumber, pageSize, sort), users.size());
      }

      private List<User> getPaginatedList(List<User> users, int pageNumber, int pageSize) {
        int fromIndex = pageNumber * pageSize;

        if (users.size() < fromIndex) {
          return Collections.emptyList();
        } else {
          int toIndex = Math.min(fromIndex + pageSize, users.size());

          return users.subList(fromIndex, toIndex);
        }
      }

      private List<User> getSortedUsers(List<User> users, Sort sort) {
        if (!sort.isSorted()) {
          return users;
        }

        List<User> sortedUsers = new ArrayList<>(users);

        for (Sort.Order order : sort) {
          Comparator<User> comparator;

          switch (order.getProperty()) {
            case "id":
              comparator = Comparator.comparing(User::getId);
              break;
            case "name":
              comparator = Comparator.comparing(User::getName);
              break;
            case "email":
              comparator = Comparator.comparing(User::getEmail);
              break;
            default:
              throw new IllegalArgumentException("Invalid sort property: " + order.getProperty());
          }

          if (order.getDirection() == Sort.Direction.DESC) {
            comparator = comparator.reversed();
          }

          sortedUsers = sortedUsers.stream()
              .sorted(comparator)
              .collect(Collectors.toList());
        }

        return sortedUsers;
      }

      Optional<User> findById(Integer id) {
        return users.stream().filter(getById(id)).findFirst();
      }

      User create(User user) {
        int newId = users.isEmpty() ? 1 : users.getLast().getId() + 1;

        user.setId(newId);
        users.add(user);

        return user;
      }

      Optional<User> update(Integer id, User userDetails) {
        return users.stream().filter(getById(id)).findFirst()
            .map(user -> {
              user.setName(userDetails.getName());
              user.setEmail(userDetails.getEmail());

              return user;
            });
      }

      boolean delete(Integer id) {
        return users.removeIf(getById(id));
      }

      private Predicate<? super User> getById(Integer id) {
        return u -> u.getId().equals(id);
      }

    }
    ```

1. Create the REST Controller.

    Create a REST controller with GET, POST, PUT, and DELETE endpoints, including some sample users:

    ```java
    ...
    @RestController
    @RequestMapping("/api/v1/users")
    public class UserController {

      private final UserService userService;

      public UserController(UserService userService) {
        this.userService = userService;
      }

      @GetMapping("")
      List<User> findAll() {
        return userService.findAll();
      }

      @GetMapping("/all")
      public Page<User> all(Pageable pageable) {
        return userService.paginateAndSort(pageable);
      }

      @GetMapping("/{id}")
      public ResponseEntity<User> findById(@PathVariable Integer id) {
        Optional<User> user = userService.findById(id);

        return user.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
      }

      @PostMapping
      public ResponseEntity<User> create(@RequestBody User user) {
        User createdUser = userService.create(user);

        return ResponseEntity.ok(createdUser);
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

1. Test the Endpoints.

    You can use tools like `Postman` or `curl` to test the endpoints:

    1. Start your Spring Boot API Rest application.

    - Get all users.
      ```bash
      curl -X GET http://localhost:8080/api/v1/users
      ```

    - Get paginated and sorted users.
      ```bash
      curl -X GET "http://localhost:8080/api/v1/users/all?page=1&size=3&sort=name,asc"
      ```

    - Get a user by ID.
      ```bash
      curl -X GET http://localhost:8080/api/v1/users/1
      ```

    - Create a user.
      ```bash
      curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john.doe@example.com"}'
      ```

    - Update a user.
      ```bash
      curl -X PUT http://localhost:8080/api/v1/users/11 -H "Content-Type: application/json" -d '{"name":"Jane Doe","email":"jane.doe@example.com"}'
      ```

    - Delete a user.
      ```bash
      curl -X DELETE http://localhost:8080/api/v1/users/11
      ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
