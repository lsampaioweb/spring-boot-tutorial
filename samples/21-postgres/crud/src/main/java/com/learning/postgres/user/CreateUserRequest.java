package com.learning.postgres.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for creating a user.
 */
public record CreateUserRequest(
    @NotBlank(message = "error.validation.name.required") String name,
    @NotBlank(message = "error.validation.email.required") @Email(message = "error.validation.email.invalid") String email) {
}
