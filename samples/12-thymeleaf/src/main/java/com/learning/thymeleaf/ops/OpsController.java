package com.learning.thymeleaf.ops;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ops")
public class OpsController {

  @GetMapping
  public String getPage(Model model) {
    model.addAttribute("ops", new OperatingSystem());

    return "ops/form";
  }

  @PostMapping
  public String postPage(@ModelAttribute("ops") OperatingSystem ops, Model model) {
    String attributeValue = ops.getOS1() + " " + ops.getOS2() + " " + ops.getOS3();

    model.addAttribute("message", attributeValue);

    return "message";
  }
}
