package com.learning.geography.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Registers PaginationConfigurationProperties as a Spring bean.
 *
 * This ensures the bean is available in both full-context and @WebMvcTest slice
 * contexts,
 * where @ConfigurationPropertiesScan on the application class is not active.
 */
@Configuration
@EnableConfigurationProperties(PaginationConfigurationProperties.class)
class PaginationConfig {
}
