package com.learning.vault.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(VaultConfigurationProperties.class)
public class VaultConfiguration {

  private static final String HEADER_ACCEPT_VALUE = "application/json";
  private static final String HEADER_VAULT_TOKEN = "X-Vault-Token";

  @Bean
  RestClient vaultRestClient(VaultConfigurationProperties properties) {
    return RestClient.builder()
        .baseUrl(properties.uri())
        .defaultHeader(HttpHeaders.ACCEPT, HEADER_ACCEPT_VALUE)
        .defaultHeader(HEADER_VAULT_TOKEN, properties.token())
        .build();
  }
}
