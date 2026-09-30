This guide demonstrates centralized exception handling for a Spring Boot REST API. Working sample: `samples/10-exception-handling`.

The real-app contract is: one `@RestControllerAdvice`, domain errors as `AppException` (message key + `errorCode` + status), one JSON envelope (including validation), and i18n for user-facing messages.

1. Enable or disable the stack trace in error responses.

    `application.yml`:

    ```yml
    server:
      error:
        include-stacktrace: "never"
    ```

    `application-development.yml`:

    ```yml
    server:
      error:
        include-stacktrace: "always"
    ```

1. Define message properties.

    Put bundles under `resources/i18n` (`messages.properties`, `messages_pt_BR.properties`).

    ```properties
    user.notfound=User with id "{0}" was not found.
    user.exists=User with name "{0}" and email "{1}" already exists.
    error.internal.server=Internal server error.
    error.resource.not.found=The requested resource was not found.
    error.validation.failed=Validation failed.
    ```

1. Create `AppException` and feature exceptions.

    Domain misses and conflicts throw subclasses of `AppException`. The handler resolves the message key through `MessageSource`.

    ```java
    public abstract class AppException extends RuntimeException {

      private final String messageKey;
      private final String errorCode;
      private final transient Object[] args;
      private final HttpStatus status;

      protected AppException(String messageKey, String errorCode, Object[] args, HttpStatus status) {
        super(messageKey);
        this.messageKey = messageKey;
        this.errorCode = errorCode;
        this.args = args;
        this.status = status;
      }

      public String getMessageKey() {
        return messageKey;
      }

      public String getErrorCode() {
        return errorCode;
      }

      public Object[] getArgs() {
        return args;
      }

      public HttpStatus getStatus() {
        return status;
      }
    }

    public class EntityNotFoundException extends AppException {

      public EntityNotFoundException(String prefix, Object[] objects) {
        super(prefix + ".notfound", prefix.toUpperCase(Locale.ROOT) + "_NOT_FOUND", objects, HttpStatus.NOT_FOUND);
      }
    }

    public class UserNotFoundException extends EntityNotFoundException {

      public UserNotFoundException(Long id) {
        super("user", new Object[] { id });
      }
    }
    ```

1. Create the error envelope.

    Use a record. Validation failures share the same shape and fill `fields`.

    ```java
    public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String errorCode,
        String message,
        String path,
        String trace,
        List<ValidationError> fields) {
    }

    public record ValidationError(String field, String message) {
    }
    ```

1. Create one `@RestControllerAdvice`.

    ```java
    @Slf4j
    @RestControllerAdvice
    public class GlobalExceptionHandler {

      private static final String SERVER_ERROR_INCLUDE_STACKTRACE = "server.error.include-stacktrace";
      private static final String STACKTRACE_ALWAYS = "always";
      private static final String ERR_INTERNAL = "error.internal.server";
      private static final String ERR_RESOURCE_NOT_FOUND = "error.resource.not.found";
      private static final String ERR_VALIDATION_FAILED = "error.validation.failed";
      private static final String LOG_UNHANDLED = "log.exception.unhandled";
      private static final String CODE_INTERNAL = "INTERNAL_ERROR";
      private static final String CODE_RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
      private static final String CODE_VALIDATION = "VALIDATION_ERROR";

      private final Environment environment;
      private final MessageSource messageSource;

      public GlobalExceptionHandler(Environment environment, MessageSource messageSource) {
        this.environment = environment;
        this.messageSource = messageSource;
      }

      @ExceptionHandler(NoResourceFoundException.class)
      @ResponseStatus(HttpStatus.NOT_FOUND)
      public @ResponseBody ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex,
          HttpServletRequest request) {
        String message = messageSource.getMessage(ERR_RESOURCE_NOT_FOUND, null, LocaleContextHolder.getLocale());

        return newErrorResponse(CODE_RESOURCE_NOT_FOUND, message, ex, request, HttpStatus.NOT_FOUND, null);
      }

      @ExceptionHandler(AppException.class)
      public ResponseEntity<ErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());
        ErrorResponse body = newErrorResponse(ex.getErrorCode(), message, ex, request, ex.getStatus(), null);

        return ResponseEntity.status(ex.getStatus()).body(body);
      }

      @ExceptionHandler(MethodArgumentNotValidException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public @ResponseBody ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
          HttpServletRequest request) {
        List<ValidationError> fields = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
            .toList();
        String message = messageSource.getMessage(ERR_VALIDATION_FAILED, null, LocaleContextHolder.getLocale());

        return newErrorResponse(CODE_VALIDATION, message, ex, request, HttpStatus.BAD_REQUEST, fields);
      }

      @ExceptionHandler(Exception.class)
      @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
      public @ResponseBody ErrorResponse handleGenericException(Exception ex, HttpServletRequest request) {
        log.error(messageSource.getMessage(LOG_UNHANDLED, null, Locale.ENGLISH), ex);
        String message = messageSource.getMessage(ERR_INTERNAL, null, LocaleContextHolder.getLocale());

        return newErrorResponse(CODE_INTERNAL, message, ex, request, HttpStatus.INTERNAL_SERVER_ERROR, null);
      }

      private ErrorResponse newErrorResponse(String errorCode, String message, Exception ex, HttpServletRequest request,
          HttpStatus httpStatus, List<ValidationError> fields) {
        return new ErrorResponse(
            OffsetDateTime.now(ZoneOffset.UTC),
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            errorCode,
            message,
            request.getRequestURI(),
            shouldIncludeStackTrace() ? getStackTraceAsString(ex) : null,
            fields);
      }

      private boolean shouldIncludeStackTrace() {
        String includeStackTrace = environment.getProperty(SERVER_ERROR_INCLUDE_STACKTRACE, "never").toLowerCase();

        return STACKTRACE_ALWAYS.equals(includeStackTrace);
      }

      private String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        return sw.toString();
      }
    }
    ```

1. Throw from the service, not the controller.

    Controllers return `ResponseEntity` and call the service. Domain misses throw `UserNotFoundException` / `UserAlreadyExistsException`. Collection GETs use Spring `Pageable` / `Page`. Domain and DTOs are records.

1. Test the endpoints.

    1. Start `samples/10-exception-handling`.

    - Missing resource:
      ```bash
      curl -X GET http://localhost:8080/api/v1/users/1111
      curl -X GET http://localhost:8080/api/v1/wrongpage
      ```

    - Conflict on duplicate create:
      ```bash
      curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john.doe@example.com"}'
      curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john.doe@example.com"}'
      ```

    - Validation failure:
      ```bash
      curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"name":"","email":"bad"}'
      ```

    - Locale:
      ```bash
      curl -X GET http://localhost:8080/api/v1/users/1111 -H "Accept-Language: pt-BR"
      ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
