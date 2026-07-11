package com.learning.geography.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

  private static final String SERVER_ERROR_INCLUDE_STACKTRACE = "server.error.include-stacktrace";
  private static final String STACKTRACE_ALWAYS = "always";
  private static final String ERR_INTERNAL = "error.internal.server";
  private static final String ERR_RESOURCE_NOT_FOUND = "error.resource.not.found";
  private static final String LOG_INTERNAL_SERVER_ERROR = "log.internal.server.error";

  private static final String CODE_RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
  private static final String CODE_COUNTRY_NOT_FOUND = "COUNTRY_NOT_FOUND";
  private static final String CODE_STATE_NOT_FOUND = "STATE_NOT_FOUND";
  private static final String CODE_CITY_NOT_FOUND = "CITY_NOT_FOUND";
  private static final String CODE_DATABASE_ERROR = "DATABASE_ERROR";
  private static final String CODE_INTERNAL_ERROR = "INTERNAL_ERROR";

  private static final String KEY_COUNTRY_NOT_FOUND = "error.country.not.found";
  private static final String KEY_STATE_NOT_FOUND = "error.state.not.found";
  private static final String KEY_CITY_NOT_FOUND = "error.city.not.found";
  private static final String KEY_COUNTRY_INSERT = "error.country.insert";
  private static final String KEY_COUNTRY_UPDATE = "error.country.update";
  private static final String KEY_COUNTRY_DELETE = "error.country.delete";
  private static final String KEY_STATE_INSERT = "error.state.insert";
  private static final String KEY_STATE_UPDATE = "error.state.update";
  private static final String KEY_STATE_DELETE = "error.state.delete";
  private static final String KEY_CITY_INSERT = "error.city.insert";
  private static final String KEY_CITY_UPDATE = "error.city.update";
  private static final String KEY_CITY_DELETE = "error.city.delete";

  private final MessageSource messageSource;
  private final Environment environment;

  GlobalExceptionHandler(MessageSource messageSource, Environment environment) {
    this.messageSource = messageSource;
    this.environment = environment;
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex,
      HttpServletRequest request) {
    String message = messageSource.getMessage(ERR_RESOURCE_NOT_FOUND, null, LocaleContextHolder.getLocale());

    return newErrorResponse(message, CODE_RESOURCE_NOT_FOUND, ex, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
    String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());

    return ResponseEntity.status(ex.getStatus())
        .body(newErrorResponse(message, resolveErrorCode(ex), ex, request, ex.getStatus()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public @ResponseBody List<ValidationError> handleValidationError(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new ValidationError(
            error.getField(),
            messageSource.getMessage(error.getDefaultMessage(), null, error.getDefaultMessage(),
                LocaleContextHolder.getLocale())))
        .toList();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ErrorResponse handleGenericError(Exception ex, HttpServletRequest request) {
    log.error(messageSource.getMessage(LOG_INTERNAL_SERVER_ERROR, null, Locale.ENGLISH), ex);
    String message = messageSource.getMessage(ERR_INTERNAL, null, LocaleContextHolder.getLocale());

    return newErrorResponse(message, CODE_INTERNAL_ERROR, ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse newErrorResponse(String message, String errorCode, Exception ex, HttpServletRequest request,
      HttpStatus status) {
    return new ErrorResponse(
        OffsetDateTime.now(ZoneOffset.UTC),
        status.value(),
        status.getReasonPhrase(),
        errorCode,
        message,
        request.getRequestURI(),
        shouldIncludeStackTrace() ? getStackTraceAsString(ex) : null);
  }

  private String resolveErrorCode(AppException ex) {
    String messageKey = ex.getMessageKey();

    if (KEY_COUNTRY_NOT_FOUND.equals(messageKey)) {
      return CODE_COUNTRY_NOT_FOUND;
    }
    if (KEY_STATE_NOT_FOUND.equals(messageKey)) {
      return CODE_STATE_NOT_FOUND;
    }
    if (KEY_CITY_NOT_FOUND.equals(messageKey)) {
      return CODE_CITY_NOT_FOUND;
    }
    if (KEY_COUNTRY_INSERT.equals(messageKey)
        || KEY_COUNTRY_UPDATE.equals(messageKey)
        || KEY_COUNTRY_DELETE.equals(messageKey)
        || KEY_STATE_INSERT.equals(messageKey)
        || KEY_STATE_UPDATE.equals(messageKey)
        || KEY_STATE_DELETE.equals(messageKey)
        || KEY_CITY_INSERT.equals(messageKey)
        || KEY_CITY_UPDATE.equals(messageKey)
        || KEY_CITY_DELETE.equals(messageKey)) {
      return CODE_DATABASE_ERROR;
    }

    return CODE_INTERNAL_ERROR;
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
