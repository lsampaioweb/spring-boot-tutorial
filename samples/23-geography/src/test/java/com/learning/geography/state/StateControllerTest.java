package com.learning.geography.state;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.geography.common.PagedResponse;
import com.learning.geography.common.PaginationConfigurationProperties;

@WebMvcTest(StateController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StateControllerTest {

  @TestConfiguration
  static class Config {
    @Bean
    PaginationConfigurationProperties paginationConfigurationProperties() {
      return new PaginationConfigurationProperties(20, 100);
    }
  }

  @Autowired
  MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean
  StateService stateService;

  @Test
  void getAllStates_whenNoAuth_shouldReturn200WithPagedList() throws Exception {
    StateResponse response = new StateResponse(1, 10, "São Paulo", "SP");
    PagedResponse<StateResponse> pagedResponse = new PagedResponse<>(List.of(response), 0, 20, 1, 1);
    BDDMockito.given(stateService.findAll(0, 20)).willReturn(pagedResponse);

    mockMvc.perform(get(StateController.PATH).param("page", "0").param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items[0].id").value(1))
        .andExpect(jsonPath("$.items[0].name").value("São Paulo"))
        .andExpect(jsonPath("$.totalElements").value(1));
    mockMvc.perform(get(StateController.PATH).param("page", "0").param("size", "20"))
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().exists("Link"));
  }

  @Test
  void getStateById_whenFound_shouldReturn200() throws Exception {
    StateResponse response = new StateResponse(1, 10, "São Paulo", "SP");
    BDDMockito.given(stateService.findById(1)).willReturn(response);

    mockMvc.perform(get(StateController.PATH + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.countryId").value(10))
        .andExpect(jsonPath("$.name").value("São Paulo"));
  }

  @Test
  void getStateById_whenNotFound_shouldReturn404() throws Exception {
    BDDMockito.given(stateService.findById(999)).willThrow(new StateNotFoundException(999));

    mockMvc.perform(get(StateController.PATH + "/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void createState_whenValidRequestAndEditor_shouldReturn201() throws Exception {
    CreateStateRequest request = new CreateStateRequest(10, "São Paulo", "SP");
    StateResponse response = new StateResponse(1, 10, "São Paulo", "SP");
    BDDMockito.given(stateService.create(any(CreateStateRequest.class))).willReturn(response);

    mockMvc.perform(post(StateController.PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().exists("Location"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("São Paulo"));
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void createState_whenBlankName_shouldReturn400() throws Exception {
    CreateStateRequest request = new CreateStateRequest(10, "", "SP");

    mockMvc.perform(post(StateController.PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  // NOTE: Spring Boot 4.x @WebMvcTest does not apply the SecurityFilterChain to
  // MockMvc.
  // Unauthenticated (401) and forbidden (403) scenarios require a full
  // @SpringBootTest context.
  // See GeographyApplicationTests for context-level security smoke test.

  @Test
  @WithMockUser(roles = "EDITOR")
  void updateState_whenValidRequestAndEditor_shouldReturn200() throws Exception {
    UpdateStateRequest request = new UpdateStateRequest(10, "São Paulo Updated", "SU");
    StateResponse response = new StateResponse(1, 10, "São Paulo Updated", "SU");
    BDDMockito.given(stateService.update(anyInt(), any(UpdateStateRequest.class))).willReturn(response);

    mockMvc.perform(put(StateController.PATH + "/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("São Paulo Updated"));
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void updateState_whenNotFoundAndEditor_shouldReturn404() throws Exception {
    UpdateStateRequest request = new UpdateStateRequest(10, "São Paulo", "SP");
    BDDMockito.given(stateService.update(anyInt(), any(UpdateStateRequest.class)))
        .willThrow(new StateNotFoundException(999));

    mockMvc.perform(put(StateController.PATH + "/999")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void deleteState_whenFoundAndEditor_shouldReturn204() throws Exception {
    BDDMockito.willDoNothing().given(stateService).delete(anyInt());

    mockMvc.perform(delete(StateController.PATH + "/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void deleteState_whenNotFoundAndEditor_shouldReturn404() throws Exception {
    BDDMockito.willThrow(new StateNotFoundException(999)).given(stateService).delete(anyInt());

    mockMvc.perform(delete(StateController.PATH + "/999"))
        .andExpect(status().isNotFound());
  }
}
