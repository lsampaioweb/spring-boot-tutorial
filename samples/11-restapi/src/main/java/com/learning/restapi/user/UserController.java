package com.learning.restapi.user;

import java.net.URI;
import java.util.Objects;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;
  private final PagedResourcesAssembler<UserResponse> pagedResourcesAssembler;

  public UserController(UserService userService, PagedResourcesAssembler<UserResponse> pagedResourcesAssembler) {
    this.userService = userService;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<UserResponse>>> findAll(Pageable pageable) {
    Page<UserResponse> users = userService.findAll(pageable);
    PagedModel<EntityModel<UserResponse>> pagedModel = pagedResourcesAssembler.toModel(Objects.requireNonNull(users));

    return ResponseEntity.ok(pagedModel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
    Optional<UserResponse> user = userService.findById(id);

    return user.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request, UriComponentsBuilder uriBuilder) {
    UserResponse createdUser = userService.create(request);

    URI location = getLocation(uriBuilder, "/{id}", createdUser.id());

    return ResponseEntity.created(Objects.requireNonNull(location)).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
    Optional<UserResponse> updatedUser = userService.update(id, request);

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
    return uriBuilder.path(Objects.requireNonNull(path))
        .buildAndExpand(Objects.requireNonNull(uriVariableValues))
        .toUri();
  }
}
