package com.learning.rabbitmq.order;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.learning.rabbitmq.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageConsumer {

  private static final String LOG_ORDER_RECEIVED = "log.order.received";
  private static final String LOG_ORDER_DETAILS = "log.order.details";
  private static final String LOG_ORDER_PROCESSED = "log.order.processed";
  private static final String LOG_ORDER_PROCESSING_FAILED = "log.order.processing.failed";
  private static final String LOG_ORDER_PROCESSING_ERROR = "log.order.processing.error";

  private final LogMessages logMessages;

  MessageConsumer(LogMessages logMessages) {
    this.logMessages = logMessages;
  }

  @RabbitListener(queues = "${app.rabbitmq.queue:orders.queue}")
  public void consumeOrder(OrderMessage message) {
    try {
      log.info(logMessages.get(LOG_ORDER_RECEIVED, message.orderId(), message.customerName()));
      log.debug(logMessages.get(LOG_ORDER_DETAILS, message.product(), message.quantity(), message.price()));

      // Simulate processing.
      Thread.sleep(500);

      log.info(logMessages.get(LOG_ORDER_PROCESSED, message.orderId()));
    } catch (InterruptedException e) {
      log.error(logMessages.get(LOG_ORDER_PROCESSING_FAILED, message.orderId()), e);
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      log.error(logMessages.get(LOG_ORDER_PROCESSING_ERROR, message.orderId()), e);
    }
  }
}
