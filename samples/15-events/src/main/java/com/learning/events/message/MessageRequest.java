package com.learning.events.message;

import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
    @NotBlank(message = "{error.validation.sender.required}") String sender,
    @NotBlank(message = "{error.validation.content.required}") String content) {
}
