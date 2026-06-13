package com.learning.websocket.client.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class WebSocketClientPageRoutes {

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("serverWsUrl", "http://localhost:8090/ws");

    return "index";
  }

}
