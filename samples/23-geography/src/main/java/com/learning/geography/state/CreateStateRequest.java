package com.learning.geography.state;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateStateRequest(
        @NotNull(message = "error.validation.country_id.required") Integer countryId,
        @NotBlank(message = "error.validation.name.required") @Size(max = 100, message = "error.validation.name.size") String name,
        @NotBlank(message = "error.validation.abbreviation.required") @Size(max = 10, message = "error.validation.abbreviation.size") String abbreviation) {
}
