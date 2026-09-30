package com.learning.rabbitmq.order;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.learning.rabbitmq.i18n.LogMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class OrderServiceImpl implements OrderService {

  private static final String LOG_ORDER_SUBMITTED = "log.order.submitted";
  private static final String API_MESSAGE_SUBMITTED_SUCCESS = "api.message.submitted.success";

  private final MessageProducer messageProducer;
  private final MessageSource messageSource;
  private final LogMessages logMessages;

  @Override
  public OrderResponse submit(OrderRequest request) {
    String orderId = UUID.randomUUID().toString();
    OrderMessage message = new OrderMessage(
        orderId,
        request.customerName(),
        request.product(),
        request.quantity(),
        request.price(),
        LocalDateTime.now(ZoneOffset.UTC));

    messageProducer.sendOrder(message, request.headerValue());
    log.info(logMessages.get(LOG_ORDER_SUBMITTED, orderId));

    return new OrderResponse(orderId, messageSource.getMessage(
        API_MESSAGE_SUBMITTED_SUCCESS, null, LocaleContextHolder.getLocale()));
  }
}
