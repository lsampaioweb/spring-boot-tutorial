package com.learning.vault.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Enables binding for Vault integration properties. */
@Configuration
@EnableConfigurationProperties(VaultConfigurationProperties.class)
public class VaultConfiguration {
}
