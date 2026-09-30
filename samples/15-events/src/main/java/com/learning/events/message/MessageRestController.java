package com.learning.events.message;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Tag(name = "{openapi.messages.tag}")
class MessageRestController {

  private final MessageService messageService;

  @PostMapping
  @Operation(summary = "{openapi.messages.publish.summary}")
  public ResponseEntity<MessageResponse> publish(@Valid @RequestBody MessageRequest request) {
    return ResponseEntity.accepted().body(messageService.publish(request));
  }
}
