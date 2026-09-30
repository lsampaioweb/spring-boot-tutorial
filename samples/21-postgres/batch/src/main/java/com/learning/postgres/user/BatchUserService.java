package com.learning.postgres.user;

/**
 * Service interface for batch user operations.
 */
interface BatchUserService {

  /**
   * Batch create multiple users.
   */
  BatchOperationResponse batchCreate(BatchCreateUserRequest request);
}
