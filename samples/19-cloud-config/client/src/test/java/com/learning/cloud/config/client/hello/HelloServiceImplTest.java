package com.learning.cloud.config.client.hello;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.learning.cloud.config.client.i18n.LogMessages;

@ExtendWith(MockitoExtension.class)
class HelloServiceImplTest {

  @Mock
  private HelloConfigurationProperties properties;

  @Mock
  private HelloMapper helloMapper;

  @Mock
  private LogMessages logMessages;

  @Mock
  private MessageSource messageSource;

  @InjectMocks
  private HelloServiceImpl helloService;

  @Test
  void sayHelloShouldMapConfiguredPropertiesIntoResponse() {
    Hello hello = new Hello("development", 8080);
    when(helloMapper.toDomain(properties)).thenReturn(hello);
    when(logMessages.get("log.say.hello")).thenReturn("Saying hello with Cloud Config properties");
    when(messageSource.getMessage(eq("response.message"), any(Object[].class), any(Locale.class)))
        .thenReturn("Message: development - 8080");
    when(helloMapper.toResponse("Message: development - 8080"))
        .thenReturn(new HelloResponse("Message: development - 8080"));

    HelloResponse response = helloService.sayHello();

    assertThat(response.message()).isEqualTo("Message: development - 8080");
    verify(helloMapper).toDomain(properties);
    verify(helloMapper).toResponse("Message: development - 8080");
  }
}
