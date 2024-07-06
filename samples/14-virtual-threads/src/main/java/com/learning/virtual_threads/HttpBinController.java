package com.learning.virtual_threads;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/httpbin")
@Slf4j
public class HttpBinController {

  private static final String HTTPBIN_BASE_URL = "https://httpbin.org/";

  private final RestClient restClient;

  public HttpBinController(RestClient.Builder restClientBuilder) {
    restClient = restClientBuilder.baseUrl(HTTPBIN_BASE_URL).build();
  }

  @GetMapping("/block/{seconds}")
  public String delay(@PathVariable int seconds) {
    ResponseEntity<Void> result = restClient
        .get()
        .uri("/delay/" + seconds)
        .retrieve()
        .toBodilessEntity();

    String message = String.format("%d on %s", result.getStatusCode().value(), Thread.currentThread());

    log.info(message);

    return message;
  }

}
