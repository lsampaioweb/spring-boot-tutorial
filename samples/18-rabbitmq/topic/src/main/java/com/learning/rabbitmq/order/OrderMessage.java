package com.learning.rabbitmq.order;

import java.time.LocalDateTime;

public record OrderMessage(
    String orderId,
    String customerName,
    String product,
    int quantity,
    double price,
    LocalDateTime createdAt) {
}
