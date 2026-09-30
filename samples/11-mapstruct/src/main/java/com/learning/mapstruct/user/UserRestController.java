package com.learning.mapstruct.user;

import java.net.URI;
import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "{openapi.users.tag}")
class UserRestController {

  private final UserService userService;

  @GetMapping
  @Operation(summary = "{openapi.users.findAll.summary}")
  public ResponseEntity<List<UserResponse>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "{openapi.users.findById.summary}")
  public ResponseEntity<UserResponse> findById(@PathVariable @Positive Long id) {
    UserResponse user = userService.findById(id);

    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(user);
  }

  @PostMapping
  @Operation(summary = "{openapi.users.create.summary}")
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request,
      UriComponentsBuilder uriBuilder) {
    UserResponse createdUser = userService.create(request);
    URI location = uriBuilder.path("/{id}").buildAndExpand(createdUser.id()).toUri();

    return ResponseEntity.created(location).body(createdUser);
  }

  @PutMapping("/{id}")
  @Operation(summary = "{openapi.users.update.summary}")
  public ResponseEntity<UserResponse> update(@PathVariable @Positive Long id,
      @Valid @RequestBody UserRequest request) {
    UserResponse updatedUser = userService.update(id, request);

    if (updatedUser == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "{openapi.users.delete.summary}")
  public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
    if (userService.delete(id)) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.notFound().build();
  }
}
