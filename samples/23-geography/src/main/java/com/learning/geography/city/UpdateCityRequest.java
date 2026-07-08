package com.learning.geography.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCityRequest(
    @NotNull(message = "error.validation.state_id.required") Long stateId,
    @NotBlank(message = "error.validation.name.required") String name) {
}
