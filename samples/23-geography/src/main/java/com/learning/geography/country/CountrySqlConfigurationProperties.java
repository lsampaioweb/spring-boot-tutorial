package com.learning.geography.country;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sql.countries")
record CountrySqlConfigurationProperties(
        String findAllPaged,
        String countAll,
        String findById,
        String insert,
        String update,
        String deleteById) {
}
