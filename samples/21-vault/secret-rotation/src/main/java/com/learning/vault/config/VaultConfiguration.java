package com.learning.vault.config;

import java.time.Duration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({
    VaultConfigurationProperties.class,
    VaultHttpClientProperties.class
})
public class VaultConfiguration {

  private static final String HEADER_ACCEPT_VALUE = "application/json";
  private static final String HEADER_VAULT_TOKEN = "X-Vault-Token";

  @Bean
  RestClient vaultRestClient(VaultConfigurationProperties properties, VaultHttpClientProperties httpProperties) {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(Duration.ofMillis(httpProperties.connectTimeoutMs()));
    requestFactory.setReadTimeout(Duration.ofMillis(httpProperties.readTimeoutMs()));

    return RestClient.builder()
        .requestFactory(requestFactory)
        .baseUrl(properties.uri())
        .defaultHeader(HttpHeaders.ACCEPT, HEADER_ACCEPT_VALUE)
        .defaultHeader(HEADER_VAULT_TOKEN, properties.token())
        .build();
  }
}
