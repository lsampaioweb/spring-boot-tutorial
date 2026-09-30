package br.com.lsampaioweb.security.openapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(properties = {
    "app.security.credentials.user.username=test-user",
    "app.security.credentials.user.password=test-user-password",
    "app.security.credentials.admin.username=test-admin",
    "app.security.credentials.admin.password=test-admin-password"
})
@ActiveProfiles("development")
class OpenApiTest {

  private final WebApplicationContext context;

  private MockMvc mockMvc;

  OpenApiTest(@Autowired WebApplicationContext context) {
    this.context = context;
  }

  @BeforeEach
  void setUp() {
    mockMvc = webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void shouldExposeApiDocsAtStablePathInDevelopment() throws Exception {
    MvcResult result = mockMvc.perform(get("/api-docs")).andReturn();

    assertThat(result.getResponse().getStatus()).isEqualTo(200);
    assertThat(mockMvc.perform(get("/v3/api-docs")).andReturn().getResponse().getStatus())
        .isEqualTo(404);
  }

  @Test
  void shouldExposeSwaggerUiAtConfiguredAndDefaultPathsInDevelopment() throws Exception {
    MvcResult result = mockMvc.perform(get("/swagger-ui.html")).andReturn();

    assertThat(result.getResponse().getStatus()).isBetween(200, 399);
    assertThat(mockMvc.perform(get("/swagger-ui/index.html")).andReturn().getResponse().getStatus())
        .isEqualTo(200);
  }
}

@SpringBootTest(properties = {
    "app.security.credentials.user.username=test-user",
    "app.security.credentials.user.password=test-user-password",
    "app.security.credentials.admin.username=test-admin",
    "app.security.credentials.admin.password=test-admin-password"
})
@ActiveProfiles("production")
class OpenApiProductionTest {

  private final WebApplicationContext context;

  private MockMvc mockMvc;

  OpenApiProductionTest(@Autowired WebApplicationContext context) {
    this.context = context;
  }

  @BeforeEach
  void setUp() {
    mockMvc = webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void shouldHideOpenApiDocumentPathsInProduction() throws Exception {
    assertThat(mockMvc.perform(get("/api-docs")).andReturn().getResponse().getStatus())
        .isEqualTo(401);
    assertThat(mockMvc.perform(get("/v3/api-docs")).andReturn().getResponse().getStatus())
        .isEqualTo(401);
    assertThat(mockMvc.perform(get("/swagger-ui.html")).andReturn().getResponse().getStatus())
        .isEqualTo(401);
    assertThat(mockMvc.perform(get("/swagger-ui/index.html")).andReturn().getResponse().getStatus())
        .isEqualTo(401);
  }
}