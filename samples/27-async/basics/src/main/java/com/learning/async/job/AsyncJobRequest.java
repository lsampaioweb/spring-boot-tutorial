package com.learning.async.job;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Describes the input and demonstration delay for a submitted job. */
public record AsyncJobRequest(
        @NotBlank(message = "{error.validation.job.input.required}") @Size(max = 500, message = "{error.validation.job.input.size}") String input,
        Boolean failForDemo,
        @Min(value = 0, message = "{error.validation.job.delay.min}") @Max(value = 5000, message = "{error.validation.job.delay.max}") long delayMs) {
}