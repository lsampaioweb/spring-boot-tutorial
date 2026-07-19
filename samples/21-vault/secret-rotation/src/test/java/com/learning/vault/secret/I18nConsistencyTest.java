package com.learning.vault.secret;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class I18nConsistencyTest {

  private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\d+}");
  private static final String EN_MESSAGES = "i18n/messages.properties";
  private static final String PT_BR_MESSAGES = "i18n/messages_pt_BR.properties";
  private static final Set<String> EXPECTED_KEYS = Set.of(
      "log.vault.registry.loading",
      "log.vault.registry.loaded",
      "log.vault.registry.secret.loaded",
      "log.vault.registry.refreshing",
      "log.vault.registry.refreshed",
      "log.vault.registry.refresh.skipped",
      "error.vault.registry.secrets.not.configured",
      "error.vault.registry.refresh.failed",
      "error.vault.registry.startup.failed",
      "error.vault.secret.key.not.found",
      "error.vault.response.empty",
      "error.vault.response.access.denied",
      "error.vault.response.path.not.found",
      "error.vault.response.server.error",
      "error.vault.response.missing.data.payload",
      "error.vault.response.missing.secret.values");

  @Test
  void localeBundlesShouldContainExactlySameKeysAndPlaceholderArity() {
    Properties en = loadProperties(EN_MESSAGES);
    Properties ptBr = loadProperties(PT_BR_MESSAGES);

    assertThat(ptBr.stringPropertyNames())
        .as("ptBR must contain all english keys")
        .containsExactlyInAnyOrderElementsOf(en.stringPropertyNames());

    assertThat(en.stringPropertyNames())
        .as("english must contain all ptBR keys")
        .containsExactlyInAnyOrderElementsOf(ptBr.stringPropertyNames());

    for (String key : en.stringPropertyNames()) {
      int enArity = placeholderCount(en.getProperty(key));
      int ptArity = placeholderCount(ptBr.getProperty(key));

      assertThat(ptArity)
          .as("placeholder arity mismatch for key: %s", key)
          .isEqualTo(enArity);
    }
  }

  @Test
  void localeBundlesShouldNotContainUnusedKeysInCode() {
    Set<String> keysInBundles = loadProperties(EN_MESSAGES).stringPropertyNames();
    Set<String> keysUsedInCode = collectI18nKeysUsedInCode();

    Set<String> unusedKeys = new HashSet<>(keysInBundles);
    unusedKeys.removeAll(keysUsedInCode);

    assertThat(unusedKeys)
        .as("i18n keys defined but not used in code")
        .isEmpty();
  }

  @Test
  void codeShouldNotReferenceMissingKeysInLocaleBundles() {
    Set<String> keysInBundles = loadProperties(EN_MESSAGES).stringPropertyNames();
    Set<String> keysUsedInCode = collectI18nKeysUsedInCode();

    Set<String> missingInBundles = new HashSet<>(keysUsedInCode);
    missingInBundles.removeAll(keysInBundles);

    assertThat(missingInBundles)
        .as("i18n keys used in code but missing in locale bundles")
        .isEmpty();
  }

  private Set<String> collectI18nKeysUsedInCode() {
    return new HashSet<>(EXPECTED_KEYS);
  }

  private Properties loadProperties(String classpathLocation) {
    Properties properties = new Properties();

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(classpathLocation)) {
      if (inputStream == null) {
        throw new IllegalStateException("Could not find properties file: " + classpathLocation);
      }

      properties.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
      return properties;
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to load properties file: " + classpathLocation, ex);
    }
  }

  private int placeholderCount(String messageTemplate) {
    Matcher matcher = PLACEHOLDER_PATTERN.matcher(messageTemplate);
    int count = 0;

    while (matcher.find()) {
      count++;
    }

    return count;
  }
}
