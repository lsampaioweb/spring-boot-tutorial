This guide will walk you through setting up a Spring Boot application with a `REST` controller that supports `GET`, `POST`, `PUT`, and `DELETE` operations.

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

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-hateoas</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Optional for endpoint monitoring -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    ```

1. Optional: Exclude Datasource Auto Configuration.

    This sample keeps an explicit datasource exclusion in `application.yml` because it does not use a database:

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
      private AtomicLong idCounter = new AtomicLong();

      public UserService() {
        users.add(new User(idCounter.incrementAndGet(), "user-01", "user-01@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-02", "user-02@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-03", "user-03@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-04", "user-04@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-05", "user-05@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-06", "user-06@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-07", "user-07@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-08", "user-08@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-09", "user-09@example.com"));
        users.add(new User(idCounter.incrementAndGet(), "user-10", "user-10@example.com"));
      }

      Page<User> findAll(Pageable pageable) {
        List<User> paginatedUsers = getPaginatedList(users, pageable);
        List<User> sortedPaginatedUsers = getSortedUsers(paginatedUsers, pageable);

        return new PageImpl<>(sortedPaginatedUsers, pageable, users.size());
      }

      Optional<User> findById(Long id) {
        return users.stream().filter(getById(id)).findFirst();
      }

      User create(User user) {
        user.setId(idCounter.incrementAndGet());

        users.add(user);

        return user;
      }

      Optional<User> update(Long id, User userDetails) {
        return users.stream().filter(getById(id)).findFirst()
            .map(user -> {
              user.setName(userDetails.getName());
              user.setEmail(userDetails.getEmail());

              return user;
            });
      }

      boolean delete(Long id) {
        return users.removeIf(getById(id));
      }

      private Predicate<? super User> getById(Long id) {
        return u -> u.getId().equals(id);
      }

      private List<User> getPaginatedList(List<User> users, Pageable pageable) {
        int fromIndex = pageable.getPageNumber() * pageable.getPageSize();

        if (users.size() < fromIndex) {
          return Collections.emptyList();
        } else {
          int toIndex = Math.min(fromIndex + pageable.getPageSize(), users.size());

          return users.subList(fromIndex, toIndex);
        }
      }

      private List<User> getSortedUsers(List<User> users, Pageable pageable) {
        Sort sort = pageable.getSort();

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
              throw new IllegalArgumentException(messageSource.getMessage(
                  "error.sort.property.invalid",
                  new Object[] { order.getProperty() },
                  Locale.ENGLISH));
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
      private final PagedResourcesAssembler<UserResponse> pagedResourcesAssembler;

      public UserController(UserService userService, PagedResourcesAssembler<UserResponse> pagedResourcesAssembler) {
        this.userService = userService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
      }

      @GetMapping
      public ResponseEntity<PagedModel<EntityModel<UserResponse>>> findAll(Pageable pageable) {
        Page<UserResponse> users = userService.findAll(pageable);
        PagedModel<EntityModel<UserResponse>> pagedModel = pagedResourcesAssembler.toModel(users);

        return ResponseEntity.ok(pagedModel);
      }

      @GetMapping("/{id}")
      public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        Optional<UserResponse> user = userService.findById(id);

        return user.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
      }

      @PostMapping
      public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request, UriComponentsBuilder uriBuilder) {
        UserResponse createdUser = userService.create(request);

        URI location = getLocation(uriBuilder, "/{id}", createdUser.id());

        return ResponseEntity.created(location).body(createdUser);
      }

      @PutMapping("/{id}")
      public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        Optional<UserResponse> updatedUser = userService.update(id, request);

        return updatedUser.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean userRemoved = userService.delete(id);

        if (userRemoved) {
          return ResponseEntity.noContent().build();
        } else {
          return ResponseEntity.notFound().build();
        }
      }

      private URI getLocation(UriComponentsBuilder uriBuilder, String path, Object... uriVariableValues) {
        return uriBuilder.path(path).buildAndExpand(uriVariableValues).toUri();
      }

    }
    ```

1. Test the Endpoints.

    You can use tools like `Postman` or `curl` to test the endpoints:

    1. Start your Spring Boot API Rest application.

    - Get all users.
      ```bash
      curl -X GET http://localhost:8080/api/v1/users
      ```

    - Get paginated and sorted users.
      ```bash
      curl -X GET "http://localhost:8080/api/v1/users?page=1&size=3&sort=name,asc"
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
