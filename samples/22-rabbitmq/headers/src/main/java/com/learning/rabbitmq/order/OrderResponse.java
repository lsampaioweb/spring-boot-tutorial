package com.learning.rabbitmq.order;

public record OrderResponse(String orderId, String message) {
}
