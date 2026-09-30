package com.learning.postgres.user;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for batch user operations.
 */
@RestController
@RequestMapping("/api/v1/users/batch")
@Tag(name = "{openapi.users.batch.tag}")
class BatchUserRestController {

  private final BatchUserService batchUserService;

  BatchUserRestController(BatchUserService batchUserService) {
    this.batchUserService = batchUserService;
  }

  /**
   * Batch create multiple users.
   */
  @PostMapping
  @Operation(summary = "{openapi.users.batch.create.summary}")
  public ResponseEntity<BatchOperationResponse> batchCreateUsers(
      @Valid @RequestBody BatchCreateUserRequest request,
      UriComponentsBuilder uriBuilder) {
    BatchOperationResponse created = batchUserService.batchCreate(request);
    URI location = uriBuilder.replacePath("/api/v1/users").build().toUri();

    return ResponseEntity.created(location).body(created);
  }
}
