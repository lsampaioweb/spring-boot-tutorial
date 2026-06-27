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

  private static final String LOG_ORDER_SENT = "log.order.sent";
  private static final String LOG_ORDER_SEND_FAILED = "log.order.send.failed";

  private final RabbitTemplate rabbitTemplate;
  private final RabbitMQConfigurationProperties rabbitMQConfigurationProperties;
  private final LogMessages logMessages;

  public void sendOrder(OrderMessage message, String headerValue) {
    try {
      rabbitTemplate.convertAndSend(rabbitMQConfigurationProperties.getExchange(), "", message, sentMessage -> {
        sentMessage
            .getMessageProperties()
            .setHeader(rabbitMQConfigurationProperties.getHeaderName(), resolveHeaderValue(headerValue));

        return sentMessage;
      });

      log.info(logMessages.get(LOG_ORDER_SENT, message.orderId(), message.customerName()));
    } catch (AmqpException e) {
      log.error(logMessages.get(LOG_ORDER_SEND_FAILED, message.orderId()), e);
      throw new OrderPublishException(e);
    }
  }

  private String resolveHeaderValue(String headerValue) {
    if (headerValue == null || headerValue.isBlank()) {
      return rabbitMQConfigurationProperties.getHeaderValueOne();
    }

    return headerValue;
  }
}
