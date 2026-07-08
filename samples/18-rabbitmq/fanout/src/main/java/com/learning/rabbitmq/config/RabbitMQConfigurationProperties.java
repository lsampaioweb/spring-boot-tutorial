package com.learning.rabbitmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.rabbitmq")
public record RabbitMQConfigurationProperties(
    String exchange,
    String queue,
    String queueOne,
    String queueTwo,
    String routingKey,
    String routingKeyOne,
    String routingKeyTwo,
    String headerName,
    String headerValueOne,
    String headerValueTwo) {
}
