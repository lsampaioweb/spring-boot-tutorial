package com.learning.geography.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Pagination defaults configurable via application.yml.
 *
 * app.pagination.default-page-size — items per page when the caller omits the
 * size param (default: 20)
 * app.pagination.max-page-size — maximum items per page the API will honour
 * (default: 100)
 */
@ConfigurationProperties(prefix = "app.pagination")
public record PaginationConfigurationProperties(int defaultPageSize, int maxPageSize) {
}
