package com.learning.websocket.lifecycle.session;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
class SessionRestController {

  private final SessionService sessionService;

  @GetMapping
  ResponseEntity<List<SessionResponse>> listSessions() {
    return ResponseEntity.ok(sessionService.listSessions());
  }

  @DeleteMapping("/{sessionId}")
  ResponseEntity<Void> forceDisconnect(@PathVariable String sessionId) {
    sessionService.forceDisconnect(sessionId);
    return ResponseEntity.noContent().build();
  }
}
