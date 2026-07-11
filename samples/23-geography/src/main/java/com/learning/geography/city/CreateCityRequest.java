package com.learning.geography.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCityRequest(
    @NotNull(message = "error.validation.state_id.required") Integer stateId,
    @NotBlank(message = "error.validation.name.required") @Size(max = 100, message = "error.validation.name.size") String name) {
}
