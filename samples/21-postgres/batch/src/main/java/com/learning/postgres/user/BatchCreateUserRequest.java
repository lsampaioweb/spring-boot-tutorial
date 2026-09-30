package com.learning.postgres.user;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * Request DTO for batch inserting users.
 */
record BatchCreateUserRequest(
    @Valid @NotEmpty(message = "error.validation.batch.users.required") List<UserData> users) {

  /**
   * Individual user data for batch insert.
   */
  public record UserData(
      @NotBlank(message = "error.validation.name.required") String name,
      @NotBlank(message = "error.validation.email.required") @Email(message = "error.validation.email.invalid") String email) {
  }
}
