package com.learning.mapstruct.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
    @NotBlank(message = "{error.validation.name.required}") String name,
    @NotBlank(message = "{error.validation.email.required}") @Email(message = "{error.validation.email.invalid}") String email) {
}
