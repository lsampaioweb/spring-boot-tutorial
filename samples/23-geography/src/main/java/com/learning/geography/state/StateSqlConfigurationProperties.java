package com.learning.geography.state;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sql.states")
record StateSqlConfigurationProperties(
        String findAllPaged,
        String countAll,
        String findById,
        String insert,
        String update,
        String deleteById) {
}
