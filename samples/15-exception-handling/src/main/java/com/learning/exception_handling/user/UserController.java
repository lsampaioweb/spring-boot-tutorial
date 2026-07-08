package com.learning.exception_handling.user;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import jakarta.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
class UserController {

  private final MessageSource messageSource;
  private final UserService service;

  public UserController(UserService service, MessageSource messageSource) {
    this.messageSource = messageSource;
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> findAll() {
    log.info(getMethodCalledMessage("findAll"));

    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
    log.info(getMethodCalledMessage("findById"));

    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request, UriComponentsBuilder uriBuilder) {
    log.info(getMethodCalledMessage("create"));

    UserResponse createdEntity = service.create(request);

    URI location = getLocation(uriBuilder, "/{id}", createdEntity.id());

    return ResponseEntity.created(Objects.requireNonNull(location)).body(createdEntity);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
    log.info(getMethodCalledMessage("update"));

    UserResponse updatedEntity = service.update(id, request);

    return ResponseEntity.ok(updatedEntity);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info(getMethodCalledMessage("delete"));

    service.delete(id);

    return ResponseEntity.noContent().build();
  }

  private URI getLocation(UriComponentsBuilder uriBuilder, String path, Object... uriVariableValues) {
    return uriBuilder.path(Objects.requireNonNull(path))
        .buildAndExpand(Objects.requireNonNull(uriVariableValues))
        .toUri();
  }

  private String getMethodCalledMessage(String methodName) {
    return messageSource.getMessage("method.called", new Object[] { methodName }, getLocale());
  }

  private Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }

}
