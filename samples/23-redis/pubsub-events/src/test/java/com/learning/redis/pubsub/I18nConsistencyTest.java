package com.learning.redis.pubsub;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class I18nConsistencyTest {

  private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\d+}");
  private static final String EN_MESSAGES = "i18n/messages.properties";
  private static final String PT_BR_MESSAGES = "i18n/messages_pt_BR.properties";

  @Test
  void localeBundlesShouldContainExactlySameKeysAndPlaceholderArity() {
    Properties en = loadProperties(EN_MESSAGES);
    Properties ptBr = loadProperties(PT_BR_MESSAGES);

    assertThat(ptBr.stringPropertyNames())
        .as("pt_BR must contain all English keys")
        .containsExactlyInAnyOrderElementsOf(en.stringPropertyNames());

    assertThat(en.stringPropertyNames())
        .as("English must contain all pt_BR keys")
        .containsExactlyInAnyOrderElementsOf(ptBr.stringPropertyNames());

    for (String key : en.stringPropertyNames()) {
      int enArity = placeholderCount(en.getProperty(key));
      int ptArity = placeholderCount(ptBr.getProperty(key));

      assertThat(ptArity)
          .as("placeholder arity mismatch for key: %s", key)
          .isEqualTo(enArity);
    }
  }

  private Properties loadProperties(String classpathLocation) {
    Properties properties = new Properties();

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(classpathLocation)) {
      if (inputStream == null) {
        throw new IllegalStateException("Could not find properties file: " + classpathLocation);
      }

      properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
      return properties;
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to load properties file: " + classpathLocation, ex);
    }
  }

  private int placeholderCount(String messageTemplate) {
    if (messageTemplate == null) {
      return 0;
    }

    Matcher matcher = PLACEHOLDER_PATTERN.matcher(messageTemplate);
    int count = 0;

    while (matcher.find()) {
      count++;
    }

    return count;
  }
}
