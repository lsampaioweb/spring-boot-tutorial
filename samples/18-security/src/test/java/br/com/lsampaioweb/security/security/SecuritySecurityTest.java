package br.com.lsampaioweb.security.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;

import org.slf4j.MDC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(properties = {
    "app.security.credentials.user.username=test-user",
    "app.security.credentials.user.password=test-user-password",
    "app.security.credentials.admin.username=test-admin",
    "app.security.credentials.admin.password=test-admin-password"
})
@ActiveProfiles("development")
class SecuritySecurityTest {

  private static final String USERNAME = "test-user";
  private static final String USER_PASSWORD = "test-user-password";
  private static final String ADMIN_USERNAME = "test-admin";
  private static final String ADMIN_PASSWORD = "test-admin-password";

  private final WebApplicationContext context;
  private final FilterChainProxy filterChainProxy;
  private final SecurityService securityService;
  private final TraceIdFilter traceIdFilter;

  private MockMvc mockMvc;

  SecuritySecurityTest(
      @Autowired WebApplicationContext context,
      @Autowired FilterChainProxy filterChainProxy,
      @Autowired SecurityService securityService,
      @Autowired TraceIdFilter traceIdFilter) {
    this.context = context;
    this.filterChainProxy = filterChainProxy;
    this.securityService = securityService;
    this.traceIdFilter = traceIdFilter;
  }

  @BeforeEach
  void setUp() {
    mockMvc = webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void shouldAllowAnonymousAccessToPublicRoute() throws Exception {
    assertThat(perform(get("/api/v1/security/public")).getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  void shouldRequireAuthenticationForAnonymousProfileRequest() throws Exception {
    assertThat(perform(get("/api/v1/security/profile")).getResponse().getStatus()).isEqualTo(401);
  }

  @Test
  void shouldRequireAuthenticationForAnonymousAdminRequest() throws Exception {
    assertThat(perform(get("/api/v1/security/admin")).getResponse().getStatus()).isEqualTo(401);
  }

  @Test
  void shouldAllowUserAccessToProfileRoute() throws Exception {
    MvcResult result = perform(get("/api/v1/security/profile").with(httpBasic(USERNAME, USER_PASSWORD)));

    assertThat(result.getResponse().getStatus()).isEqualTo(200);
    assertThat(result.getResponse().getContentAsString()).contains(USERNAME);
  }

  @Test
  void shouldForbidUserAccessToAdminRoute() throws Exception {
    assertThat(perform(get("/api/v1/security/admin").with(httpBasic(USERNAME, USER_PASSWORD)))
        .getResponse().getStatus()).isEqualTo(403);
  }

  @Test
  void shouldAllowAdminAccessToAdminRoute() throws Exception {
    assertThat(perform(get("/api/v1/security/admin").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
        .getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  void shouldRejectPublicRouteSuffixBoundary() throws Exception {
    assertThat(perform(get("/api/v1/security/public/extra")).getResponse().getStatus()).isEqualTo(401);
  }

  @Test
  void shouldDenyAuthenticatedAccessToUnmatchedRoute() throws Exception {
    assertThat(perform(get("/api/v1/security/unknown").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
        .getResponse().getStatus()).isEqualTo(403);
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldDenyUserByMethodAuthorizationOnAdminService() {
    assertThatThrownBy(securityService::getAdminMessage)
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void shouldDisableCsrfFilterForStatelessBasicConfiguration() {
    List<Filter> filters = filterChainProxy.getFilters("/api/v1/security/profile");

    assertThat(filters).noneMatch(CsrfFilter.class::isInstance);
  }

  @Test
  void shouldAllowAdminPostToProfileRouteWithoutCsrf() throws Exception {
    assertThat(perform(post("/api/v1/security/profile").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
        .getResponse().getStatus()).isEqualTo(405);
  }

  @Test
  void shouldClearMdcWhenTraceIdFilterCompletesRequest() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = (requestInChain, responseInChain) -> assertThat(MDC.get("traceId")).isNotBlank();

    traceIdFilter.doFilter(request, response, filterChain);

    assertThat(MDC.get("traceId")).isNull();
  }

  private MvcResult perform(RequestBuilder request)
      throws Exception {
    return mockMvc.perform(request).andReturn();
  }
}