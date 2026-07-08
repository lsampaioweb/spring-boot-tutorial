package com.learning.geography.city;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sql.cities")
record CitySqlConfigurationProperties(
    String findAll,
    String findById,
    String insert,
    String update,
    String deleteById) {
}
