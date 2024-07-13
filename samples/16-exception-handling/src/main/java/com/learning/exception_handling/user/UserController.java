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
