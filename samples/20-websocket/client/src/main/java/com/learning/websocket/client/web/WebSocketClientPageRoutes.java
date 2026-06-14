package com.learning.websocket.client.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class WebSocketClientPageRoutes {

  private static final String MODEL_SERVER_WS_URL = "serverWsUrl";

  private final WebSocketClientConfigurationProperties webSocketClientConfigurationProperties;

  public WebSocketClientPageRoutes(WebSocketClientConfigurationProperties webSocketClientConfigurationProperties) {
    this.webSocketClientConfigurationProperties = webSocketClientConfigurationProperties;
  }

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute(MODEL_SERVER_WS_URL, webSocketClientConfigurationProperties.serverWsUrl());

    return "index";
  }

}
