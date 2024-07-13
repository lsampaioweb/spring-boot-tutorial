package com.learning.exception_handling.advice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.learning.exception_handling.user.NoSuchUserExistsException;
import com.learning.exception_handling.user.UserAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.env.Environment;

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

  // @ExceptionHandler(NoSuchUserExistsException.class)
  // public ResponseEntity<String>
  // handleNoSuchUserExistsException(NoSuchUserExistsException ex) {
  // return ResponseEntity
  // .status(HttpStatus.NOT_FOUND)
  // .body(ex.getMessage());
  // }

  // @ExceptionHandler(UserAlreadyExistsException.class)
  // public ResponseEntity<String>
  // handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
  // return ResponseEntity
  // .status(HttpStatus.CONFLICT)
  // .body(ex.getMessage());
  // }

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
    PrintWriter pw = new PrintWriter(sw);

    ex.printStackTrace(pw);

    return sw.toString();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<FieldError> errors = ex.getFieldErrors();

    return ResponseEntity
        // .status(HttpStatus.BAD_REQUEST)
        .badRequest()
        .body(errors.stream().map(FieldsWithError::new).toList());
  }

  private record FieldsWithError(String field, String message) {

    public FieldsWithError(FieldError error) {
      this(error.getField(), error.getDefaultMessage());
    }
  }

}
