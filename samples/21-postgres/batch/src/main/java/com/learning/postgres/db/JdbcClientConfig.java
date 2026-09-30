package com.learning.postgres.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Loads feature SQL from classpath XML. Boot provides {@code JdbcClient} and
 * {@code NamedParameterJdbcTemplate}.
 */
@Configuration
@PropertySource("classpath:sql/users.xml")
class JdbcClientConfig {
}
