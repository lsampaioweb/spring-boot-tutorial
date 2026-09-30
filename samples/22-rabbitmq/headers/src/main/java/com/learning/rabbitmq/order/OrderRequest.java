package com.learning.rabbitmq.order;

public record OrderRequest(
    String customerName,
    String product,
    int quantity,
    double price,
    String headerValue) {
}
