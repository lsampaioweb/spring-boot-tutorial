package com.learning.postgres.user;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sql.users")
record UserSqlConfigurationProperties(
    String findAll,
    String countAll,
    String findById,
    String insert,
    String update,
    String deleteById) {
}
