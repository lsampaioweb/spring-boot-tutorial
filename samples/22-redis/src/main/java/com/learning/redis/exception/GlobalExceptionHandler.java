package com.learning.redis.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String SERVER_ERROR_INCLUDE_STACKTRACE = "server.error.include-stacktrace";
  private static final String STACKTRACE_ALWAYS = "always";

  private final MessageSource messageSource;
  private final Environment environment;

  public GlobalExceptionHandler(MessageSource messageSource, Environment environment) {
    this.messageSource = messageSource;
    this.environment = environment;
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
    String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());
    ErrorResponse response = newErrorResponse(message, ex, request, ex.getStatus());

    return ResponseEntity.status(ex.getStatus()).body(response);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex,
      HttpServletRequest request) {
    return newErrorResponse(ex.getMessage(), ex, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public @ResponseBody List<ValidationError> handleValidationException(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
        .toList();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ErrorResponse handleGenericError(Exception ex, HttpServletRequest request) {
    return newErrorResponse(ex.getMessage(), ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse newErrorResponse(String message, Exception ex, HttpServletRequest request,
      HttpStatus status) {
    return new ErrorResponse(
        LocalDateTime.now(ZoneOffset.UTC),
        status.value(),
        status.getReasonPhrase(),
        message,
        request.getRequestURI(),
        shouldIncludeStackTrace() ? getStackTraceAsString(ex) : null);
  }

  private boolean shouldIncludeStackTrace() {
    String value = environment.getProperty(SERVER_ERROR_INCLUDE_STACKTRACE, "never").toLowerCase();

    return STACKTRACE_ALWAYS.equals(value);
  }

  private String getStackTraceAsString(Exception ex) {
    StringWriter sw = new StringWriter();

    ex.printStackTrace(new PrintWriter(sw));

    return sw.toString();
  }

}
