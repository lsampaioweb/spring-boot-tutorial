package com.learning.rabbitmq.order;

interface OrderService {

  OrderResponse submit(OrderRequest request);
}
