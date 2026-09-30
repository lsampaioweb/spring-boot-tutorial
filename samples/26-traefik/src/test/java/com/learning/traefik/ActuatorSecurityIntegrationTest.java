package com.learning.traefik;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import jakarta.annotation.Resource;

@SpringBootTest(properties = {
    "app.security.credentials.actuator.username=actuator",
    "app.security.credentials.actuator.password=secret"
})
class ActuatorSecurityIntegrationTest {

  @Resource
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = webAppContextSetup(webApplicationContext)
        .apply(springSecurity())
        .build();
  }

  @Test
  void getHealth_whenAnonymous_shouldReturnOk() throws Exception {
    mockMvc.perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  void getInfo_whenAnonymous_shouldReturnUnauthorized() throws Exception {
    mockMvc.perform(get("/actuator/info"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void getInfo_whenAuthenticated_shouldReturnOk() throws Exception {
    mockMvc.perform(get("/actuator/info")
        .with(httpBasic("actuator", "secret")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap());
  }

  @Test
  void getHello_whenAnonymous_shouldReturnSuccessWithoutHostLeakage() throws Exception {
    mockMvc.perform(get("/api/v1/users/hello"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", not(emptyOrNullString())))
        .andExpect(jsonPath("$.message", is("Hello from the Traefik sample.")))
        .andExpect(jsonPath("$.message", not(containsString("Hostname"))))
        .andExpect(jsonPath("$.message", not(containsString("IP Address"))))
        .andExpect(jsonPath("$.message", not(containsString("127.0.0.1"))));
  }

  @Test
  void getHello_whenLocaleIsPtBr_shouldReturnPortugueseMessage() throws Exception {
    mockMvc.perform(get("/api/v1/users/hello").header("Accept-Language", "pt-BR"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", is("Ola do exemplo Traefik.")));
  }
}