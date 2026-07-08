package com.learning.geography.country;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sql.countries")
record CountrySqlConfigurationProperties(
    String findAll,
    String findById,
    String insert,
    String update,
    String deleteById) {
}
