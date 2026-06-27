package com.learning.rabbitmq.api;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.rabbitmq.i18n.LogMessages;
import com.learning.rabbitmq.order.MessageProducer;
import com.learning.rabbitmq.order.OrderMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/messages/topic")
@RequiredArgsConstructor
class OrderApi {

  private static final String LOG_ORDER_SUBMITTED = "log.order.submitted";
  private static final String LOG_ORDER_SUBMISSION_ERROR = "log.order.submission.error";
  private static final String API_MESSAGE_SUBMITTED_SUCCESS = "api.message.submitted.success";
  private static final String API_MESSAGE_SUBMITTED_FAILURE = "api.message.submitted.failure";

  private final MessageProducer messageProducer;
  private final MessageSource messageSource;
  private final LogMessages logMessages;

  @PostMapping
  public ResponseEntity<OrderResponse> submitOrder(
      @RequestParam String customerName,
      @RequestParam String product,
      @RequestParam int quantity,
      @RequestParam double price,
      @RequestParam(required = false) String routingKey) {
    try {
      String orderId = UUID.randomUUID().toString();
      OrderMessage message = new OrderMessage(
          orderId,
          customerName,
          product,
          quantity,
          price,
          LocalDateTime.now(ZoneOffset.UTC));

      messageProducer.sendOrder(message, routingKey);
      log.info(logMessages.get(LOG_ORDER_SUBMITTED, orderId));

      return ResponseEntity.status(HttpStatus.ACCEPTED).body(
          new OrderResponse(orderId, getMessage(API_MESSAGE_SUBMITTED_SUCCESS)));
    } catch (Exception e) {
      log.error(logMessages.get(LOG_ORDER_SUBMISSION_ERROR), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new OrderResponse(null, getMessage(API_MESSAGE_SUBMITTED_FAILURE)));
    }
  }

  private String getMessage(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }

  public record OrderResponse(String orderId, String message) {
  }
}
