package com.learning.thymeleaf.web;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
class PageController {

  private static final String PAGE_MESSAGE_SERVER = "page.message.server";
  private static final String PAGE_COLLECTION_TITLE = "page.collection.title";

  private final MessageSource messageSource;

  PageController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @GetMapping("")
  public String index() {
    return "index";
  }

  @GetMapping("message")
  public String messagePage(Model model) {
    String message = messageSource.getMessage(PAGE_MESSAGE_SERVER, null, LocaleContextHolder.getLocale());

    model.addAttribute("message", message);

    return "message";
  }

  @GetMapping("collection")
  public String collectionPage(Model model) {
    String message = messageSource.getMessage(PAGE_COLLECTION_TITLE, null, LocaleContextHolder.getLocale());

    List<String> list = List.of(
        "Amazon Web Services",
        "Microsoft Azure",
        "Google Cloud",
        "Alibaba Cloud",
        "IBM Cloud");

    model.addAttribute("message", message);
    model.addAttribute("cloudProvider", list);

    return "collection";
  }
}
