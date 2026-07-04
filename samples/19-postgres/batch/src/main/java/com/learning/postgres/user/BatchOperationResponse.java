package com.learning.postgres.user;

import java.util.List;

/**
 * Response DTO for batch operations.
 */
record BatchOperationResponse(
        int totalRequested,
        int totalProcessed,
        List<UserResponse> createdUsers,
        List<UserResponse> failedUsers) {
}
