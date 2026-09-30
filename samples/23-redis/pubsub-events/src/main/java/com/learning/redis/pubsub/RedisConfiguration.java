package com.learning.redis.pubsub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Configuration
class RedisConfiguration {

  static final String PRODUCT_EVENTS_CHANNEL = "product-events";

  @Bean
  ObjectMapper objectMapper() {
    return JsonMapper.builder().findAndAddModules().build();
  }

  @Bean
  RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    StringRedisSerializer serializer = new StringRedisSerializer();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(serializer);
    template.setValueSerializer(serializer);
    template.afterPropertiesSet();
    return template;
  }

  @Bean
  MessageListenerAdapter productEventListener(ProductAuditListener listener) {
    return new MessageListenerAdapter(listener, "onProductPublished");
  }

  @Bean
  @ConditionalOnProperty(name = "redis.pubsub.listener.enabled", havingValue = "true", matchIfMissing = true)
  RedisMessageListenerContainer redisMessageListenerContainer(
      RedisConnectionFactory connectionFactory,
      MessageListenerAdapter productEventListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(productEventListener, new ChannelTopic(PRODUCT_EVENTS_CHANNEL));
    return container;
  }

}