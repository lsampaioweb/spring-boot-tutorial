package com.learning.geography.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCountryRequest(
        @NotBlank(message = "error.validation.name.required") @Size(max = 100, message = "error.validation.name.size") String name,
        @NotBlank(message = "error.validation.iso_code.required") @Size(min = 2, max = 2, message = "error.validation.iso_code.size") String isoCode) {
}
