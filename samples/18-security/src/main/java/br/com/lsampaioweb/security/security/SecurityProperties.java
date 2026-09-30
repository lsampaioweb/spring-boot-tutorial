package br.com.lsampaioweb.security.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.security")
record SecurityProperties(
    /** Environment-specific credentials for the provisioned principals. */
    @Valid @NotNull Credentials credentials) {

  public record Credentials(
      /** Username and password supplied by the USER environment variables. */
      @Valid @NotNull UserCredentials user,
      /** Username and password supplied by the ADMIN environment variables. */
      @Valid @NotNull UserCredentials admin) {
  }

  public record UserCredentials(
      /** External username for the provisioned principal. */
      @NotBlank String username,
      /** External plaintext input that is encoded before authentication. */
      @NotBlank String password) {
  }
}