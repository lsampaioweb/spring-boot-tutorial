package com.learning.geography.db;

import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@PropertySource({
    "classpath:sql/countries.xml",
    "classpath:sql/states.xml",
    "classpath:sql/cities.xml"
})
class JdbcClientConfig {

  @Bean
  JdbcClient jdbcClient(DataSource dataSource) {
    return JdbcClient.create(Objects.requireNonNull(dataSource));
  }
}
