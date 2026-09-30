package com.learning.postgres.user;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(UserSqlConfigurationProperties.class)
class UserConfiguration {
}