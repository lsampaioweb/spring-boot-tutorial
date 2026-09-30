package com.learning.redis.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import com.learning.redis.cache.product.ProductResponse;

@Configuration
@EnableCaching
class CacheConfiguration implements CachingConfigurer {

  @Bean
  CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(SerializationPair.fromSerializer(
            new JacksonJsonRedisSerializer<>(ProductResponse.class)));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(configuration)
        .build();
  }

}