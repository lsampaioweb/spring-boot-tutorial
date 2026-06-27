package com.learning.rabbitmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app.rabbitmq")
@Getter
@Setter
public class RabbitMQConfigurationProperties {

  private String exchange = "orders.fanout.exchange";

  private String queue;
  private String queueOne = "orders.fanout.queue.one";
  private String queueTwo = "orders.fanout.queue.two";

  private String routingKey;
  private String routingKeyOne;
  private String routingKeyTwo;

  private String headerName;
  private String headerValueOne;
  private String headerValueTwo;
}
