package com.learning.postgres.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Loads feature SQL from classpath XML. Boot provides the {@code JdbcClient} bean.
 */
@Configuration
@PropertySource("classpath:sql/users.xml")
class JdbcClientConfig {
}
