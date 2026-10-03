package com.learning.websocket.lifecycle.session;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
class SessionSocketController {

  private final SessionService sessionService;

  SessionSocketController(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  /** Harmless keep-alive style message used by the demo UI. */
  @MessageMapping("/lifecycle.ping")
  void ping(SimpMessageHeaderAccessor headers) {
    sessionService.handleClientMessage(headers.getSessionId());
  }

  /** Intentionally noisy destination used to demonstrate abuse disconnects. */
  @MessageMapping("/lifecycle.burst")
  void burst(SimpMessageHeaderAccessor headers) {
    sessionService.handleClientMessage(headers.getSessionId());
  }
}
