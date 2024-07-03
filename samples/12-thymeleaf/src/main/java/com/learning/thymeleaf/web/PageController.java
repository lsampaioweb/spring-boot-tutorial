package com.learning.thymeleaf.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class PageController {

  @GetMapping("")
  public String index() {
    return "index";
  }

  @GetMapping("message")
  public String messagePage(Model model) {
    String message = "Message from the Server 2";

    model.addAttribute("message", message);

    return "message";
  }

  @GetMapping("collection")
  public String collectionPage(Model model) {
    String message = "Top 5 Cloud Service Providers";

    List<String> list = new ArrayList<>();

    list.add("Amazon Web Services");
    list.add("Microsoft Azure");
    list.add("Google Cloud");
    list.add("Alibaba Cloud");
    list.add("IBM Cloud");

    model.addAttribute("message", message);
    model.addAttribute("cloudProvider", list);

    return "collection";
  }
}
