package com.learning.virtual_threads;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.virtual_threads.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/httpbins")
@Slf4j
public class HttpBinController {

  private static final String LOG_HTTPBIN_DELAY_COMPLETED = "log.httpbin.delay.completed";

  private final HttpBinService httpBinService;
  private final LogMessages logMessages;

  public HttpBinController(HttpBinService httpBinService, LogMessages logMessages) {
    this.httpBinService = httpBinService;
    this.logMessages = logMessages;
  }

  @GetMapping("/block/{seconds}")
  public ResponseEntity<DelayResponse> delay(@PathVariable int seconds) {
    DelayResponse response = httpBinService.delay(seconds);
    log.info(logMessages.get(LOG_HTTPBIN_DELAY_COMPLETED, response.statusCode(), response.thread()));

    return ResponseEntity.ok(response);
  }

}
