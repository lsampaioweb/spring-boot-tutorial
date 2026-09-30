package com.learning.rabbitmq.order;

class OrderPublishException extends RuntimeException {

  public OrderPublishException(Throwable cause) {
    super(cause);
  }
}