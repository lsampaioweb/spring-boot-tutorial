package com.learning.cloud.config.client.hello;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.hello")
record HelloConfigurationProperties(String role, int serverPort) {
}
