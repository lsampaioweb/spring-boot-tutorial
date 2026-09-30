package br.com.lsampaioweb.security.security;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(SecurityProperties.class)
class SecurityConfig {

  private final SecurityProperties securityProperties;
  private final boolean apiDocsEnabled;
  private final boolean swaggerUiEnabled;

  public SecurityConfig(
      SecurityProperties securityProperties,
      @Value("${springdoc.api-docs.enabled:false}") boolean apiDocsEnabled,
      @Value("${springdoc.swagger-ui.enabled:false}") boolean swaggerUiEnabled) {
    this.securityProperties = Objects.requireNonNull(securityProperties);
    this.apiDocsEnabled = apiDocsEnabled;
    this.swaggerUiEnabled = swaggerUiEnabled;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
        // HTTP Basic is stateless, so browser CSRF tokens are not applicable to this
        // REST sample.
        .csrf(csrf -> csrf.disable())
        .headers(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> {
          authorize.requestMatchers("/api/v1/security/public").permitAll();
          if (apiDocsEnabled || swaggerUiEnabled) {
            authorize.requestMatchers("/api-docs/**", "/v3/api-docs/**").permitAll();
            authorize.requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll();
          }
          authorize.requestMatchers("/api/v1/security/admin").hasRole("ADMIN");
          authorize.requestMatchers("/api/v1/security/profile").authenticated();
          authorize.anyRequest().denyAll();
        });

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  LocaleResolver localeResolver() {
    var resolver = new AcceptHeaderLocaleResolver();

    resolver.setDefaultLocale(Locale.ENGLISH);
    resolver.setSupportedLocales(List.of(Locale.ENGLISH, Locale.forLanguageTag("pt-BR")));

    return resolver;
  }

  @Bean
  UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    var user = securityProperties.credentials().user();
    var admin = securityProperties.credentials().admin();

    return new InMemoryUserDetailsManager(
        User.withUsername(user.username())
            .password(passwordEncoder.encode(user.password()))
            .roles("USER")
            .build(),
        User.withUsername(admin.username())
            .password(passwordEncoder.encode(admin.password()))
            .roles("ADMIN")
            .build());
  }
}