package com.learning.exception_handling.user;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

  private final UserService service;

  @GetMapping
  @Operation(summary = "{openapi.users.findAll.summary}")
  public ResponseEntity<Page<UserResponse>> findAll(
      @PageableDefault(size = 20, sort = "id") Pageable pageable) {
    return ResponseEntity.ok(service.findAll(pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "{openapi.users.findById.summary}")
  public ResponseEntity<UserResponse> findById(@PathVariable @Positive Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  @Operation(summary = "{openapi.users.create.summary}")
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request,
      UriComponentsBuilder uriBuilder) {
    UserResponse createdEntity = service.create(request);
    URI location = uriBuilder.path("/{id}").buildAndExpand(createdEntity.id()).toUri();

    return ResponseEntity.created(location).body(createdEntity);
  }

  @PutMapping("/{id}")
  @Operation(summary = "{openapi.users.update.summary}")
  public ResponseEntity<UserResponse> update(@PathVariable @Positive Long id,
      @Valid @RequestBody UserRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "{openapi.users.delete.summary}")
  public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
    service.delete(id);

    return ResponseEntity.noContent().build();
  }
}
