package com.learning.geography;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class GeographyApplicationTests {

  @Autowired
  ApplicationContext applicationContext;

  @Test
  void contextLoads_whenAllBeansAvailable_shouldStartApplicationContext() {
    assertThat(applicationContext).isNotNull();
  }
}
