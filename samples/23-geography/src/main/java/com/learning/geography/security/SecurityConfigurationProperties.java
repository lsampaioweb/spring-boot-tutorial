package com.learning.geography.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Security user credentials, injected from environment variables:
 * APP_SECURITY_USERNAME and APP_SECURITY_PASSWORD.
 */
@ConfigurationProperties(prefix = "app.security.user")
record SecurityConfigurationProperties(String username, String password) {
}
