package com.learning.geography.state;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateStateRequest(
    @NotNull(message = "error.validation.country_id.required") Long countryId,
    @NotBlank(message = "error.validation.name.required") String name,
    @NotBlank(message = "error.validation.abbreviation.required") String abbreviation) {
}
