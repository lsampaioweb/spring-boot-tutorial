package com.learning.tracing.caller.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Outbound callee connection settings for the tracing demo.
 */
@ConfigurationProperties(prefix = "app.callee")
public record CalleeProperties(String baseUrl) {
}
