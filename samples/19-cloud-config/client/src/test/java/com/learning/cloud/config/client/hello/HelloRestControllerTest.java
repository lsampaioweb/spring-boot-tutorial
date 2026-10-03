package com.learning.cloud.config.client.hello;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class HelloRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private HelloService helloService;

  @Test
  void sayHelloShouldRejectAnonymousCallers() throws Exception {
    mockMvc.perform(get("/api/v1/hellos"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void sayHelloShouldRejectCallersWithoutRequiredRole() throws Exception {
    mockMvc.perform(get("/api/v1/hellos")
        .with(user("other-user").roles("OTHER")))
        .andExpect(status().isForbidden());
  }

  @Test
  void sayHelloShouldReturnMessageForAuthenticatedCaller() throws Exception {
    when(helloService.sayHello()).thenReturn(new HelloResponse("Message: development - 8080"));

    mockMvc.perform(get("/api/v1/hellos")
        .with(user("cloud-config-api").roles("CLOUD_CONFIG_API")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Message: development - 8080"));
  }
}
