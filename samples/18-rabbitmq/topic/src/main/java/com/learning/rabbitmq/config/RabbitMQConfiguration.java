package com.learning.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
  public TopicExchange ordersExchange() {
    return new TopicExchange(rabbitMQConfigurationProperties.exchange(), true, false);
  }

  @Bean
  public Queue ordersQueueOne() {
    return new Queue(rabbitMQConfigurationProperties.queueOne(), true, false, false);
  }

  @Bean
  public Queue ordersQueueTwo() {
    return new Queue(rabbitMQConfigurationProperties.queueTwo(), true, false, false);
  }

  @Bean
  public Binding ordersBindingOne(Queue ordersQueueOne, TopicExchange ordersExchange) {
    return BindingBuilder.bind(ordersQueueOne)
        .to(ordersExchange)
        .with(rabbitMQConfigurationProperties.routingKeyOne());
  }

  @Bean
  public Binding ordersBindingTwo(Queue ordersQueueTwo, TopicExchange ordersExchange) {
    return BindingBuilder.bind(ordersQueueTwo)
        .to(ordersExchange)
        .with(rabbitMQConfigurationProperties.routingKeyTwo());
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }
}
