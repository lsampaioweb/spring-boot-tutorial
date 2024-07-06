package com.learning.virtual_threads;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class PageController {

  @GetMapping("/virtual-threads-false")
  public String page1() {
    return "virtual-threads-false";
  }

  @GetMapping("/virtual-threads-true")
  public String page2() {
    return "virtual-threads-true";
  }

}
