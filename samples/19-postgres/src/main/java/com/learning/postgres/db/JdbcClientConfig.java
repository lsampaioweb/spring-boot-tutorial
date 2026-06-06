package com.learning.postgres.db;

import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@PropertySource("classpath:sql/users.xml")
public class JdbcClientConfig {

  @Bean
  public JdbcClient jdbcClient(DataSource dataSource) {
    return JdbcClient.create(Objects.requireNonNull(dataSource));
  }

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
    return new NamedParameterJdbcTemplate(Objects.requireNonNull(dataSource));
  }
}