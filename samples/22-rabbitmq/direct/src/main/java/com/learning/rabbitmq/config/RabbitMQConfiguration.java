package com.learning.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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
  public DirectExchange ordersExchange() {
    return new DirectExchange(rabbitMQConfigurationProperties.exchange(), true, false);
  }

  @Bean
  public Queue ordersQueue() {
    return new Queue(rabbitMQConfigurationProperties.queue(), true, false, false);
  }

  @Bean
  public Binding ordersBinding(Queue ordersQueue, DirectExchange ordersExchange) {
    return BindingBuilder.bind(ordersQueue)
        .to(ordersExchange)
        .with(rabbitMQConfigurationProperties.routingKey());
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }
}
