package com.learning.exception_handling.core.i18n;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class MessageSourceHolder implements ApplicationContextAware {

  private static final AtomicReference<MessageSource> messageSource = new AtomicReference<>();

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    messageSource.set(applicationContext.getBean(MessageSource.class));
  }

  public static MessageSource getMessageSource() {
    return messageSource.get();
  }
}
