package com.learning.vault.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VaultConfigurationProperties.class)
public class VaultConfiguration {
}
