package com.learning.rabbitmq.order;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.learning.rabbitmq.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageConsumer {

  @RabbitListener(queues = "${app.rabbitmq.queue-one:orders.headers.queue.created}")
  public void consumeCreatedQueue(OrderMessage message) {
    processMessage(message);
  }

  @RabbitListener(queues = "${app.rabbitmq.queue-two:orders.headers.queue.audit}")
  public void consumeAuditQueue(OrderMessage message) {
    processMessage(message);
  }

  private void processMessage(OrderMessage message) {
    try {
      log.info(LogMessages.ORDER_RECEIVED, message.orderId(), message.customerName());
      log.debug(
          LogMessages.ORDER_DETAILS,
          message.product(),
          message.quantity(),
          message.price());

      Thread.sleep(500);

      log.info(LogMessages.ORDER_PROCESSED, message.orderId());
    } catch (InterruptedException e) {
      log.error(LogMessages.ORDER_PROCESSING_FAILED, message.orderId(), e);
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      log.error(LogMessages.ORDER_PROCESSING_ERROR, message.orderId(), e);
    }
  }
}
