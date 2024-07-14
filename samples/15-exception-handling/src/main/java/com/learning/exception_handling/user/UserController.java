package com.learning.exception_handling.user;

import java.net.URI;
import java.util.List;
import java.util.Locale;
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
public class UserController {

  private final MessageSource messageSource;
  private final UserService service;

  public UserController(UserService service, MessageSource messageSource) {
    this.messageSource = messageSource;
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<User>> findAll() {
    log.info(getMethodCalledMessage("findAll"));

    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    log.info(getMethodCalledMessage("findById"));

    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User entity, UriComponentsBuilder uriBuilder) {
    log.info(getMethodCalledMessage("create"));

    User createdEntity = service.create(entity);

    URI location = getLocation(uriBuilder, "/{id}", createdEntity.getId());

    return ResponseEntity.created(location).body(createdEntity);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User entity) {
    log.info(getMethodCalledMessage("update"));

    User updatedEntity = service.update(id, entity);

    return ResponseEntity.ok(updatedEntity);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info(getMethodCalledMessage("delete"));

    service.delete(id);

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
