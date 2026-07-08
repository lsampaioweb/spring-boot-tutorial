package com.learning.virtual_threads;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
class HttpBinServiceImpl implements HttpBinService {

  private static final String HTTPBIN_BASE_URL = "https://httpbin.org/";

  private final RestClient restClient;

  HttpBinServiceImpl(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.baseUrl(HTTPBIN_BASE_URL).build();
  }

  @Override
  public DelayResponse delay(int seconds) {
    ResponseEntity<Void> result = restClient
        .get()
        .uri("/delay/" + seconds)
        .retrieve()
        .toBodilessEntity();

    return new DelayResponse(result.getStatusCode().value(), Thread.currentThread().toString());
  }
}
