package com.learning.redis.cache.product;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sql.products")
record ProductSqlConfigurationProperties(
    String findAll,
    String findById,
    String insert,
    String update,
    String deleteById) {
}
