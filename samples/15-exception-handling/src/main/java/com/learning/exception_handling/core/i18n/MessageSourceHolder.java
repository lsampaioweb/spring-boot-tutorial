package com.learning.exception_handling.core.i18n;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceHolder implements ApplicationContextAware {

  private static MessageSource messageSource;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    messageSource = applicationContext.getBean(MessageSource.class);
  }

  public static MessageSource getMessageSource() {
    return messageSource;
  }

}
