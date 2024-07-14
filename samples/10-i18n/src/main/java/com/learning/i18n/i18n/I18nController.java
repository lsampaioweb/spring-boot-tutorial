package com.learning.i18n.i18n;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class I18nController {

  @GetMapping("")
  public String index() {
    return "index";
  }
}
