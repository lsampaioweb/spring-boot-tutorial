package com.learning.http_client.user;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

@RestController
@RequestMapping("http/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<User>>> findAll(Pageable pageable) {
    PagedModel<EntityModel<User>> users = userService.findAll(pageable);

    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable Integer id) {
    Optional<User> user = userService.findById(id);

    return user.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user, UriComponentsBuilder uriBuilder) {
    User createdUser = userService.create(user);

    URI location = uriBuilder.path("/{id}").buildAndExpand(createdUser.getId()).toUri();

    return ResponseEntity.created(location).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
    Optional<User> updatedUser = userService.update(id, user);

    return updatedUser.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    boolean userRemoved = userService.delete(id);

    if (userRemoved) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
