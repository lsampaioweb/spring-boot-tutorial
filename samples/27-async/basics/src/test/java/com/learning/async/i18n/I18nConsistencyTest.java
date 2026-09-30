package com.learning.async.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class I18nConsistencyTest {

  private static final String EN_MESSAGES = "i18n/messages.properties";
  private static final String PT_BR_MESSAGES = "i18n/messages_pt_BR.properties";
  private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\d+\\}");

  @Test
  void localeBundlesHaveMatchingKeysAndPlaceholderArity() throws IOException {
    Properties english = loadProperties(EN_MESSAGES);
    Properties portuguese = loadProperties(PT_BR_MESSAGES);

    assertThat(portuguese.stringPropertyNames())
        .containsExactlyInAnyOrderElementsOf(english.stringPropertyNames());

    for (String key : english.stringPropertyNames()) {
      assertThat(placeholderCount(portuguese.getProperty(key)))
          .as("placeholder count for %s", key)
          .isEqualTo(placeholderCount(english.getProperty(key)));
    }
  }

  private Properties loadProperties(String resourcePath) throws IOException {
    Properties properties = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      assertThat(input).as("resource %s", resourcePath).isNotNull();
      properties.load(input);
    }

    return properties;
  }

  private int placeholderCount(String message) {
    Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);
    int count = 0;

    while (matcher.find()) {
      count++;
    }

    return count;
  }
}