package com.learning.redis.cache.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

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
import lombok.extern.slf4j.Slf4j;

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

  private final MessageSource messageSource;
  private final Environment environment;

  public GlobalExceptionHandler(MessageSource messageSource, Environment environment) {
    this.messageSource = messageSource;
    this.environment = environment;
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
    String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());
    ErrorResponse response = newErrorResponse(ex.getErrorCode(), message, ex, request, ex.getStatus(), null);

    return ResponseEntity.status(ex.getStatus()).body(response);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex,
      HttpServletRequest request) {
    String message = messageSource.getMessage(ERR_RESOURCE_NOT_FOUND, null, LocaleContextHolder.getLocale());

    return newErrorResponse(CODE_RESOURCE_NOT_FOUND, message, ex, request, HttpStatus.NOT_FOUND, null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public @ResponseBody ErrorResponse handleValidationException(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    List<ValidationError> fields = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
        .toList();
    String message = messageSource.getMessage(ERR_VALIDATION_FAILED, null, LocaleContextHolder.getLocale());

    return newErrorResponse(CODE_VALIDATION, message, ex, request, HttpStatus.BAD_REQUEST, fields);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ErrorResponse handleGenericError(Exception ex, HttpServletRequest request) {
    log.error(messageSource.getMessage(LOG_UNHANDLED, null, Locale.ENGLISH), ex);
    String message = messageSource.getMessage(ERR_INTERNAL, null, LocaleContextHolder.getLocale());

    return newErrorResponse(CODE_INTERNAL, message, ex, request, HttpStatus.INTERNAL_SERVER_ERROR, null);
  }

  private ErrorResponse newErrorResponse(String errorCode, String message, Exception ex, HttpServletRequest request,
      HttpStatus status, List<ValidationError> fields) {
    return new ErrorResponse(
        OffsetDateTime.now(ZoneOffset.UTC),
        status.value(),
        status.getReasonPhrase(),
        errorCode,
        message,
        request.getRequestURI(),
        shouldIncludeStackTrace() ? getStackTraceAsString(ex) : null,
        fields);
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
