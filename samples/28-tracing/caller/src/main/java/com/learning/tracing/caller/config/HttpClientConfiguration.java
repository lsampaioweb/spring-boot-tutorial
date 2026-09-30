package com.learning.tracing.caller.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(CalleeProperties.class)
public class HttpClientConfiguration {

  /**
   * Builds a RestClient aimed at the callee service using Boot's observed builder.
   *
   * @param builder auto-configured RestClient builder (propagates trace context)
   * @param properties callee base URL settings
   * @return configured RestClient
   */
  @Bean
  RestClient calleeRestClient(RestClient.Builder builder, CalleeProperties properties) {
    return builder
        .baseUrl(properties.baseUrl())
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}
