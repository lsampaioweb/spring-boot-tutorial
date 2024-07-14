package com.learning.restapi.user;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;
  private final PagedResourcesAssembler<User> pagedResourcesAssembler;

  public UserController(UserService userService, PagedResourcesAssembler<User> pagedResourcesAssembler) {
    this.userService = userService;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<User>>> findAll(Pageable pageable) {
    Page<User> users = userService.findAll(pageable);
    PagedModel<EntityModel<User>> pagedModel = pagedResourcesAssembler.toModel(users);

    return ResponseEntity.ok(pagedModel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    Optional<User> user = userService.findById(id);

    return user.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user, UriComponentsBuilder uriBuilder) {
    User createdUser = userService.create(user);

    URI location = getLocation(uriBuilder, "/{id}", createdUser.getId());

    return ResponseEntity.created(location).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
    Optional<User> updatedUser = userService.update(id, user);

    return updatedUser.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    boolean userRemoved = userService.delete(id);

    if (userRemoved) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  private URI getLocation(UriComponentsBuilder uriBuilder, String path, Object... uriVariableValues) {
    return uriBuilder.path(path).buildAndExpand(uriVariableValues).toUri();
  }

}
