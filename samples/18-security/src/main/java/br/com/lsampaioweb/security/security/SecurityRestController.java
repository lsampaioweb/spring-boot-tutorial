package br.com.lsampaioweb.security.security;

import java.security.Principal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lsampaioweb.security.i18n.SecurityMessages;

@RestController
@RequestMapping(SecurityRestController.BASE_PATH)
@Tag(name = "{openapi.security.tag}")
class SecurityRestController {

  static final String BASE_PATH = "/api/v1/security";
  private static final String PUBLIC_MESSAGE_KEY = "api.security.public";
  private static final String PROFILE_MESSAGE_KEY = "api.security.profile";

  private final SecurityMessages messages;
  private final SecurityService securityService;

  public SecurityRestController(SecurityMessages messages, SecurityService securityService) {
    this.messages = messages;
    this.securityService = securityService;
  }

  @GetMapping("/public")
  @Operation(summary = "{openapi.security.public.summary}")
  public ResponseEntity<SecurityResponse> getPublicMessage() {
    return ResponseEntity.ok(new SecurityResponse(messages.get(PUBLIC_MESSAGE_KEY)));
  }

  @GetMapping("/profile")
  @Operation(summary = "{openapi.security.profile.summary}")
  public ResponseEntity<SecurityResponse> getProfileMessage(Principal principal) {
    return ResponseEntity.ok(new SecurityResponse(messages.get(PROFILE_MESSAGE_KEY, principal.getName())));
  }

  @GetMapping("/admin")
  @Operation(summary = "{openapi.security.admin.summary}")
  public ResponseEntity<SecurityResponse> getAdminMessage() {
    return ResponseEntity.ok(new SecurityResponse(securityService.getAdminMessage()));
  }

  record SecurityResponse(String message) {
  }
}