package com.learning.rabbitmq.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  private final RabbitMQConfigurationProperties rabbitMQConfigurationProperties;

  public RabbitMQConfiguration(RabbitMQConfigurationProperties rabbitMQConfigurationProperties) {
    this.rabbitMQConfigurationProperties = rabbitMQConfigurationProperties;
  }

  @Bean
  public HeadersExchange ordersExchange() {
    return new HeadersExchange(rabbitMQConfigurationProperties.getExchange(), true, false);
  }

  @Bean
  public Queue ordersQueueOne() {
    return new Queue(rabbitMQConfigurationProperties.getQueueOne(), true, false, false);
  }

  @Bean
  public Queue ordersQueueTwo() {
    return new Queue(rabbitMQConfigurationProperties.getQueueTwo(), true, false, false);
  }

  @Bean
  public Binding ordersBindingOne(Queue ordersQueueOne, HeadersExchange ordersExchange) {
    return BindingBuilder.bind(ordersQueueOne)
        .to(ordersExchange)
        .whereAll(createHeadersMap(rabbitMQConfigurationProperties.getHeaderValueOne()))
        .match();
  }

  @Bean
  public Binding ordersBindingTwo(Queue ordersQueueTwo, HeadersExchange ordersExchange) {
    return BindingBuilder.bind(ordersQueueTwo)
        .to(ordersExchange)
        .whereAll(createHeadersMap(rabbitMQConfigurationProperties.getHeaderValueTwo()))
        .match();
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }

  private Map<String, Object> createHeadersMap(String headerValue) {
    return Map.of(rabbitMQConfigurationProperties.getHeaderName(), headerValue);
  }
}
