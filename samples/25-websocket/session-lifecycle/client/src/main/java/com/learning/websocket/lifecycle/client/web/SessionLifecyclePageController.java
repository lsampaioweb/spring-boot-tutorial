package com.learning.websocket.lifecycle.client.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class SessionLifecyclePageController {

  private final WebSocketClientConfigurationProperties properties;

  SessionLifecyclePageController(WebSocketClientConfigurationProperties properties) {
    this.properties = properties;
  }

  @GetMapping("/")
  String index(Model model) {
    model.addAttribute("serverWsUrl", properties.serverWsUrl());
    model.addAttribute("sessionsApiUrl", properties.sessionsApiUrl());
    return "index";
  }
}
