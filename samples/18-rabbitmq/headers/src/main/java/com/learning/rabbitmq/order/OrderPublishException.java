package com.learning.rabbitmq.order;

public class OrderPublishException extends RuntimeException {

  public OrderPublishException(Throwable cause) {
    super(cause);
  }
}