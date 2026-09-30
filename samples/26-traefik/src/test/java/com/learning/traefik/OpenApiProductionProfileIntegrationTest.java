package com.learning.traefik;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
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
class OpenApiProductionProfileIntegrationTest {

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
  void getApiDocs_whenProductionProfileIsDefault_shouldBeUnavailable() throws Exception {
    mockMvc.perform(get("/v3/api-docs"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void getSwaggerUi_whenProductionProfileIsDefault_shouldBeUnavailable() throws Exception {
    mockMvc.perform(get("/swagger-ui/index.html"))
        .andExpect(status().is4xxClientError());
  }
}