package com.learning.redis.cache.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Loads feature SQL from classpath XML. Boot provides the {@code JdbcClient} bean.
 */
@Configuration
@PropertySource("classpath:sql/products.xml")
class JdbcClientConfig {
}
