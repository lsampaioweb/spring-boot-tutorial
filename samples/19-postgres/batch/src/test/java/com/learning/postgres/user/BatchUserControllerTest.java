package com.learning.postgres.user;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BatchUserController.class)
@ActiveProfiles("test")
class BatchUserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BatchUserService batchUserService;

  private BatchOperationResponse sampleResponse;

  @BeforeEach
  void setUp() {
    UserResponse createdUser = new UserResponse(1L, "John Doe", "john@example.com");
    sampleResponse = new BatchOperationResponse(1, 1, List.of(createdUser), List.of());
  }

  @Test
  void batchCreateUsers_whenValidRequest_shouldReturn201() throws Exception {
    String payload = """
        {
          \"users\": [
            {
              \"name\": \"John Doe\",
              \"email\": \"john@example.com\"
            }
          ]
        }
        """;

    given(batchUserService.batchCreate(org.mockito.ArgumentMatchers.any())).willReturn(sampleResponse);

    mockMvc.perform(post("/api/v1/users/batch")
        .contentType("application/json")
        .content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.totalRequested").value(1))
        .andExpect(jsonPath("$.totalProcessed").value(1));
  }

  @Test
  void batchCreateUsers_whenUsersMissing_shouldReturn400() throws Exception {
    String payload = "{}";

    mockMvc.perform(post("/api/v1/users/batch")
        .contentType("application/json")
        .content(payload))
        .andExpect(status().isBadRequest());
  }
}
