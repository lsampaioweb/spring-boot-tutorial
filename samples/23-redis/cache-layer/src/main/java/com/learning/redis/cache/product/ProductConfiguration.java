package com.learning.redis.cache.product;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProductSqlConfigurationProperties.class)
class ProductConfiguration {
}
