package com.learning.websocket.lifecycle.session;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SessionService sessionService;

  @Test
  void listSessionsRequiresAuthentication() throws Exception {
    mockMvc.perform(get("/api/v1/sessions"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void listSessionsReturnsActiveSessionsForAdmin() throws Exception {
    when(sessionService.listSessions())
        .thenReturn(List.of(new SessionResponse("abc", "student", Instant.parse("2026-01-01T00:00:00Z"))));

    mockMvc.perform(get("/api/v1/sessions")
        .with(user("session-admin").roles("SESSION_ADMIN")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].sessionId").value("abc"))
        .andExpect(jsonPath("$[0].displayName").value("student"));
  }

  @Test
  void forceDisconnectReturnsNoContent() throws Exception {
    mockMvc.perform(delete("/api/v1/sessions/abc")
        .with(user("session-admin").roles("SESSION_ADMIN")))
        .andExpect(status().isNoContent());

    verify(sessionService).forceDisconnect("abc");
  }

  @Test
  void forceDisconnectReturnsNotFoundForUnknownSession() throws Exception {
    doThrow(new SessionNotFoundException("missing")).when(sessionService).forceDisconnect("missing");

    mockMvc.perform(delete("/api/v1/sessions/missing")
        .with(user("session-admin").roles("SESSION_ADMIN")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value("SESSION_NOT_FOUND"));
  }
}
