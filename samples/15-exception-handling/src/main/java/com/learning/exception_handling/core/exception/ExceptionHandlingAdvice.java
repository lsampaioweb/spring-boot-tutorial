package com.learning.exception_handling.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandlingAdvice {

  private static final String SERVER_ERROR_INCLUDE_STACKTRACE = "server.error.include-stacktrace";
  private static final String SERVER_ERROR_INCLUDE_STACKTRACE_NEVER = "never";
  private static final String SERVER_ERROR_INCLUDE_STACKTRACE_ALWAYS = "always";
  private static final String SERVER_ERROR_INCLUDE_STACKTRACE_ON_TRACE_PARAM = "on_trace_param";

  private final Environment environment;
  private final MessageSource messageSource;

  public ExceptionHandlingAdvice(Environment environment, MessageSource messageSource) {
    this.environment = environment;
    this.messageSource = messageSource;
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex,
      HttpServletRequest request) {
    return newErrorResponse(ex, ex.getMessage(), request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
    String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());

    return ResponseEntity
        .status(ex.getStatus())
        .body(newErrorResponse(ex, message, request, ex.getStatus()));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ErrorResponse handleGenericException(Exception ex, HttpServletRequest request) {
    return newErrorResponse(ex, ex.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<FieldError> errors = ex.getFieldErrors();

    return ResponseEntity
        .badRequest()
        .body(errors.stream().map(FieldsWithError::new).toList());
  }

  private ErrorResponse newErrorResponse(Exception ex, String message, HttpServletRequest request,
      HttpStatus httpStatus) {
    return new ErrorResponse(
        LocalDateTime.now(),
        httpStatus.value(),
        httpStatus.getReasonPhrase(),
        message,
        request.getRequestURI(),
        shouldIncludeStackTrace() ? getStackTraceAsString(ex) : null);
  }

  private boolean shouldIncludeStackTrace() {
    String includeStackTrace = environment.getProperty(SERVER_ERROR_INCLUDE_STACKTRACE,
        SERVER_ERROR_INCLUDE_STACKTRACE_NEVER).toLowerCase();

    return SERVER_ERROR_INCLUDE_STACKTRACE_ALWAYS.equals(includeStackTrace)
        || SERVER_ERROR_INCLUDE_STACKTRACE_ON_TRACE_PARAM.equals(includeStackTrace);
  }

  private String getStackTraceAsString(Exception ex) {
    StringWriter sw = new StringWriter();

    ex.printStackTrace(new PrintWriter(sw));

    return sw.toString();
  }

  private record FieldsWithError(String field, String message) {

    public FieldsWithError(FieldError error) {
      this(error.getField(), error.getDefaultMessage());
    }
  }

}
