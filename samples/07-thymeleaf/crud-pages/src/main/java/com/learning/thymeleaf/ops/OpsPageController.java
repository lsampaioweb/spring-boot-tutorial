package com.learning.thymeleaf.ops;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ops")
class OpsPageController {

  @GetMapping
  public String getPage(Model model) {
    model.addAttribute("ops", new OperatingSystem());

    return "ops/form";
  }

  @PostMapping
  public String postPage(@ModelAttribute("ops") OperatingSystem ops, Model model) {
    String attributeValue = ops.getOs1() + " " + ops.getOs2() + " " + ops.getOs3();

    model.addAttribute("message", attributeValue);

    return "message";
  }
}
