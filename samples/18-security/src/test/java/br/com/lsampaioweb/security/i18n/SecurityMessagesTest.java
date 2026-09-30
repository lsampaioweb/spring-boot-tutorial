package br.com.lsampaioweb.security.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;

class SecurityMessagesTest {

  private static final String DEFAULT_BUNDLE = "i18n/messages.properties";
  private static final String PORTUGUESE_BUNDLE = "i18n/messages_pt_BR.properties";
  private static final Pattern PLACEHOLDER = Pattern.compile("\\{(\\d+)}");
  private static final Set<String> REQUIRED_KEYS = Set.of(
      "api.security.public",
      "api.security.profile",
      "api.security.admin",
      "openapi.info.title",
      "openapi.info.description",
      "openapi.security.tag",
      "openapi.security.public.summary",
      "openapi.security.profile.summary",
      "openapi.security.admin.summary");

  @Test
  void shouldHaveIdenticalKeysAndPlaceholdersAcrossBundles() throws IOException {
    Properties english = load(DEFAULT_BUNDLE);
    Properties portuguese = load(PORTUGUESE_BUNDLE);

    assertThat(portuguese.stringPropertyNames()).containsExactlyInAnyOrderElementsOf(english.stringPropertyNames());
    assertThat(english.stringPropertyNames()).containsExactlyInAnyOrderElementsOf(REQUIRED_KEYS);
    for (String key : english.stringPropertyNames()) {
      assertThat(placeholderIndexes(english.getProperty(key)))
          .as("placeholder indexes for %s", key)
          .containsExactlyInAnyOrderElementsOf(placeholderIndexes(portuguese.getProperty(key)));
    }
  }

  @Test
  void shouldResolveEnglishPortugueseAndFallbackLocales() {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setBasenames("i18n/messages");
    source.setDefaultEncoding(StandardCharsets.UTF_8.name());
    SecurityMessages messages = new SecurityMessages(source);

    assertThat(messages.get(Locale.ENGLISH, "api.security.public")).contains("public");
    assertThat(messages.get(Locale.forLanguageTag("pt-BR"), "api.security.public"))
        .contains("público");
    assertThat(messages.get(Locale.forLanguageTag("fr-FR"), "api.security.public"))
        .isEqualTo(messages.get(Locale.ENGLISH, "api.security.public"));
    assertThat(messages.get(Locale.ENGLISH, "openapi.info.title")).isNotBlank();
    assertThat(messages.get(Locale.forLanguageTag("pt-BR"), "openapi.info.title")).isNotBlank();
  }

  private Properties load(String path) throws IOException {
    Properties properties = new Properties();
    ClassPathResource resource = new ClassPathResource(path);
    try (InputStream input = resource.getInputStream();
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
      properties.load(reader);
    }
    return properties;
  }

  private Set<String> placeholderIndexes(String message) {
    var matcher = PLACEHOLDER.matcher(message);
    var indexes = new java.util.HashSet<String>();
    while (matcher.find()) {
      indexes.add(matcher.group(1));
    }
    return indexes;
  }
}