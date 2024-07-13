This guide demonstrates how to handle exceptions in a Spring Boot application. The goal is to provide meaningful error messages to clients and maintain a clean and maintainable codebase.

1. Enable or disable trace in the exception message:

    `application.yml` file:

    ```yml
    # This is the default, so it is not necessary.
    server:
      error:
        include-stacktrace: "never"
    ```

    `application-dev.yml` file:
    ```yml
    server:
      error:
        include-stacktrace: "always"
    ```

1. Define Message Properties.

    Create message properties files for each locale (`messages.properties`, `messages_pt_BR.properties`, etc.) within `resources/i18n`.

    messages.properties:
    ```properties
    method.called=The method "{0}" was called.
    user.notfound=User with id "{0}" was not found.
    user.exists=User with name "{0}" and email "{1}" already exists.
    ```

    messages_pt_BR.properties:
    ```properties
    method.called=O método "{0}" foi chamado.
    user.notfound=Usuário com id "{0}" não foi encontrado.
    user.exists=Usuário com nome "{0}" e email "{1}" já existe.
    ```

1. Creating Custom Exceptions.

    - When the user does not exists.
      ```java
      public class NoSuchUserExistsException extends RuntimeException {
        public NoSuchUserExistsException(String message) {
          super(message);
        }
      }
      ```

    - When the user already exists.
      ```java
      public class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
          super(message);
        }
      }
      ```

1. Creating the ErrorResponse class.

      ```java
      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public class ErrorResponse {

        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private String trace;
      }
      ```

1. Exception Handling.

    - Create a centralized exception handling class using `@RestControllerAdvice`.
      ```java
      @RestControllerAdvice
      public class ExceptionHandling {

        private static final String SERVER_ERROR_INCLUDE_STACKTRACE = "server.error.include-stacktrace";
        private static final String SERVER_ERROR_INCLUDE_STACKTRACE_NEVER = "never";
        private static final String SERVER_ERROR_INCLUDE_STACKTRACE_ALWAYS = "always";
        private static final String SERVER_ERROR_INCLUDE_STACKTRACE_ON_TRACE_PARAM = "on_trace_param";

        private final Environment environment;

        public ExceptionHandling(Environment environment) {
          this.environment = environment;
        }

        @ExceptionHandler(NoResourceFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public @ResponseBody ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex,
            HttpServletRequest request) {
          return newErrorResponse(ex, request, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(NoSuchUserExistsException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public @ResponseBody ErrorResponse handleNoSuchUserExistsException(NoSuchUserExistsException ex,
            HttpServletRequest request) {
          return newErrorResponse(ex, request, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(UserAlreadyExistsException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public @ResponseBody ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex,
            HttpServletRequest request) {
          return newErrorResponse(ex, request, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(Exception.class)
        public @ResponseBody ErrorResponse handleGenericException(Exception ex, HttpServletRequest request) {
          return newErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        private ErrorResponse newErrorResponse(Exception ex, HttpServletRequest request,
            HttpStatus httpStatus) {
          boolean shouldIncludeStackTrace = shouldIncludeStackTrace();

          return new ErrorResponse(
              LocalDateTime.now(),
              httpStatus.value(),
              httpStatus.getReasonPhrase(),
              ex.getMessage(),
              request.getRequestURI(),
              shouldIncludeStackTrace ? getStackTraceAsString(ex) : null);
        }

        private boolean shouldIncludeStackTrace() {
          String includeStackTrace = environment.getProperty(SERVER_ERROR_INCLUDE_STACKTRACE,
              SERVER_ERROR_INCLUDE_STACKTRACE_NEVER);

          return (SERVER_ERROR_INCLUDE_STACKTRACE_ALWAYS.equalsIgnoreCase(includeStackTrace))
              || (SERVER_ERROR_INCLUDE_STACKTRACE_ON_TRACE_PARAM.equalsIgnoreCase(includeStackTrace));
        }

        private String getStackTraceAsString(Exception ex) {
          StringWriter sw = new StringWriter();
          ex.printStackTrace(new PrintWriter(sw));

          return sw.toString();
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
          List<FieldError> errors = ex.getFieldErrors();

          return ResponseEntity
              .badRequest()
              .body(errors.stream().map(FieldsWithError::new).toList());
        }

        private record FieldsWithError(String field, String message) {
          public FieldsWithError(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
          }
        }
      }
      ```

1. Creating the User Service.

      ```java
      @Service
      class UserService {

        private final MessageSource messageSource;

        private List<User> users = new ArrayList<>();
        private AtomicLong idCounter = new AtomicLong();

        public UserService(MessageSource messageSource) {
          this.messageSource = messageSource;

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

        List<User> findAll() {
          return users;
        }

        User findById(Long id) {
          Optional<User> user = users.stream().filter(getById(id)).findFirst();

          if (user.isPresent()) {
            return user.get();
          } else {
            throw new NoSuchUserExistsException(getUserNotFoundMessage(id));
          }
        }

        User create(User user) {
          boolean userExists = users.stream().anyMatch(equals(user));

          if (userExists) {
            throw new UserAlreadyExistsException(getUserAlreadyExistsMessage(user));
          } else {
            user.setId(idCounter.incrementAndGet());

            users.add(user);

            return user;
          }
        }

        User update(Long id, User userDetails) {
          User user = findById(id);

          user.setName(userDetails.getName());
          user.setEmail(userDetails.getEmail());

          return user;
        }

        boolean delete(Long id) {
          User user = findById(id);

          return users.remove(user);
        }

        private Predicate<? super User> getById(Long id) {
          return u -> u.getId().equals(id);
        }

        private Predicate<? super User> equals(User user) {
          return u -> u.getName().equals(user.getName()) &&
              u.getEmail().equals(user.getEmail());
        }

        private Locale getLocale() {
          return LocaleContextHolder.getLocale();
        }

        private String getUserNotFoundMessage(Long id) {
          return messageSource.getMessage("user.notfound", new Object[] { id }, getLocale());
        }

        private String getUserAlreadyExistsMessage(User user) {
          return messageSource.getMessage("user.exists", new Object[] { user.getName(), user.getEmail() }, getLocale());
        }
      }
      ```

1. Creating the User Controller.

      ```java
      @RestController
      @RequestMapping("/api/v1/users")
      @Slf4j
      public class UserController {

        private final MessageSource messageSource;
        private final UserService userService;

        public UserController(UserService userService, MessageSource messageSource) {
          this.messageSource = messageSource;
          this.userService = userService;
        }

        @GetMapping
        public ResponseEntity<List<User>> findAll() {
          log.info(getMethodCalledMessage("findAll"));

          return ResponseEntity.ok(userService.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<User> findById(@PathVariable Long id) {
          log.info(getMethodCalledMessage("findById"));

          return ResponseEntity.ok(userService.findById(id));
        }

        @PostMapping
        public ResponseEntity<User> create(@RequestBody User user, UriComponentsBuilder uriBuilder) {
          log.info(getMethodCalledMessage("create"));

          User createdUser = userService.create(user);

          URI location = getLocation(uriBuilder, "/{id}", createdUser.getId());

          return ResponseEntity.created(location).body(createdUser);
        }

        @PutMapping("/{id}")
        public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
          log.info(getMethodCalledMessage("update"));

          User updatedUser = userService.update(id, user);

          return ResponseEntity.ok(updatedUser);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
          log.info(getMethodCalledMessage("delete"));

          userService.delete(id);

          return ResponseEntity.ok().build();
        }

        private URI getLocation(UriComponentsBuilder uriBuilder, String path, Object... uriVariableValues) {
          return uriBuilder.path(path).buildAndExpand(uriVariableValues).toUri();
        }

        private String getMethodCalledMessage(String methodName) {
          return messageSource.getMessage("method.called", new Object[] { methodName }, getLocale());
        }

        private Locale getLocale() {
          return LocaleContextHolder.getLocale();
        }
      }
      ```

1. Test the Endpoints.

    You can use tools like `Postman` or `curl` to test the endpoints:

    1. Start your Spring Boot application.

    - Get all users:
      ```bash
      curl -X GET http://localhost:8080/api/v1/users
      ```

    - Try fetching a user by ID that does not exist:
      ```bash
      curl -X GET http://localhost:8080/api/v1/users/1111
      ```

    - Try accessing an endpoint that does not exist:
      ```bash
      curl -X GET http://localhost:8080/api/v1/wrongpage
      ```
    - Try creating a user:
      - This endpoint creates a new user with the provided JSON payload containing the user's name and email.
        ```bash
        curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john.doe@example.com"}'
        ```

    - Try creating a user twice:
      - This endpoint attempts to create a user with the same name and email as an existing user. It should return a `409 Conflict` error indicating that the user already exists.
        ```bash
        curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john.doe@example.com"}'
        ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
