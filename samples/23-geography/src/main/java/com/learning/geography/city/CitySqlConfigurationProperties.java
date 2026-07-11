package com.learning.geography.city;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sql.cities")
record CitySqlConfigurationProperties(
    String findAllPaged,
    String countAll,
    String findById,
    String insert,
    String update,
    String deleteById) {
}
