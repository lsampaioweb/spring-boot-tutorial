package com.learning.websocket.client.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
class WebSocketClientPageController {

  private static final String MODEL_SERVER_WS_URL = "serverWsUrl";

  private final WebSocketClientConfigurationProperties webSocketClientConfigurationProperties;

  public WebSocketClientPageController(WebSocketClientConfigurationProperties webSocketClientConfigurationProperties) {
    this.webSocketClientConfigurationProperties = webSocketClientConfigurationProperties;
  }

  /** Adds the configured WebSocket server URL and selects the chat page. */
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute(MODEL_SERVER_WS_URL, webSocketClientConfigurationProperties.serverWsUrl());

    return "index";
  }

}
