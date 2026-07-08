package com.learning.postgres.user;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sql.users")
record UserSqlConfigurationProperties(
    String findAll,
    String findById,
    String insert,
    String update,
    String deleteById) {
}
