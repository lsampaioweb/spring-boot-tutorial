package com.learning.async.job;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.UUID;

import com.learning.async.core.exception.AsyncJobNotFoundException;

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
class AsyncJobRestControllerTest {

  private static final String JOBS_PATH = "/api/v1/jobs";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AsyncJobService asyncJobService;

  @Test
  void submitReturnsAcceptedAndLocationForAuthenticatedCaller() throws Exception {
    UUID id = UUID.randomUUID();
    when(asyncJobService.submit(new AsyncJobRequest("hello", false, 250)))
        .thenReturn(response(id, JobStatus.QUEUED, null, null));

    mockMvc.perform(post(JOBS_PATH)
        .with(user("async-user").roles("ASYNC_USER"))
        .contentType("application/json")
        .content("{\"input\":\"hello\",\"delayMs\":250}"))
        .andExpect(status().isAccepted())
        .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith(id.toString())))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.status").value("QUEUED"));
  }

  @Test
  void submitRejectsInvalidInput() throws Exception {
    mockMvc.perform(post(JOBS_PATH)
        .with(user("async-user").roles("ASYNC_USER"))
        .contentType("application/json")
        .content("{\"input\":\" \"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
        .andExpect(jsonPath("$.fields[0].field").value("input"));
  }

  @Test
  void jobEndpointsRequireAuthentication() throws Exception {
    mockMvc.perform(get(JOBS_PATH + "/" + UUID.randomUUID()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void jobEndpointsRejectCallersWithoutTheRequiredRole() throws Exception {
    mockMvc.perform(get(JOBS_PATH + "/" + UUID.randomUUID())
        .with(user("other-user").roles("OTHER")))
        .andExpect(status().isForbidden());
  }

  @Test
  void findReturnsLocalizedNotFoundEnvelope() throws Exception {
    UUID id = UUID.randomUUID();
    when(asyncJobService.find(id)).thenThrow(new AsyncJobNotFoundException(id));

    mockMvc.perform(get(JOBS_PATH + "/" + id)
        .with(user("async-user").roles("ASYNC_USER")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value("ASYNC_JOB_NOT_FOUND"));
  }

  private AsyncJobResponse response(UUID id, JobStatus status, String result, String failureCode) {
    Instant now = Instant.now();

    return new AsyncJobResponse(id, status, result, failureCode, now, null, null);
  }
}