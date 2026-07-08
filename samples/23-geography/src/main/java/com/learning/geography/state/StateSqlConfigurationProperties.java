package com.learning.geography.state;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sql.states")
record StateSqlConfigurationProperties(
    String findAll,
    String findById,
    String insert,
    String update,
    String deleteById) {
}
