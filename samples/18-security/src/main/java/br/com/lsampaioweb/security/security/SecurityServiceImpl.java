package br.com.lsampaioweb.security.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import br.com.lsampaioweb.security.i18n.SecurityMessages;

@Service
class SecurityServiceImpl implements SecurityService {

  private static final String ADMIN_MESSAGE_KEY = "api.security.admin";

  private final SecurityMessages messages;

  SecurityServiceImpl(SecurityMessages messages) {
    this.messages = messages;
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public String getAdminMessage() {
    return messages.get(ADMIN_MESSAGE_KEY);
  }
}