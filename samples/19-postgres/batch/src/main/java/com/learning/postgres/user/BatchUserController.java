package com.learning.postgres.user;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for batch user operations.
 */
@RestController
@RequestMapping("/api/v1/users/batch")
class BatchUserController {

  private final BatchUserService batchUserService;

  BatchUserController(BatchUserService batchUserService) {
    this.batchUserService = batchUserService;
  }

  /**
   * Batch create multiple users.
   */
  @PostMapping
  public ResponseEntity<BatchOperationResponse> batchCreateUsers(
      @Valid @RequestBody BatchCreateUserRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(batchUserService.batchCreate(request));
  }
}
