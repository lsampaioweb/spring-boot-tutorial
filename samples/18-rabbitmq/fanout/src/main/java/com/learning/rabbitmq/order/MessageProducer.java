package com.learning.rabbitmq.order;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.learning.rabbitmq.config.RabbitMQConfigurationProperties;
import com.learning.rabbitmq.i18n.LogMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitMQConfigurationProperties rabbitMQConfigurationProperties;

  public void sendOrder(OrderMessage message) {
    try {
      rabbitTemplate.convertAndSend(rabbitMQConfigurationProperties.getExchange(), "", message);

      log.info(LogMessages.ORDER_SENT, message.orderId(), message.customerName());
    } catch (AmqpException e) {
      log.error(LogMessages.ORDER_SEND_FAILED, message.orderId(), e);
      throw new OrderPublishException(e);
    }
  }
}
