package com.learning.geography.city;

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

@WebMvcTest(CityController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CityControllerTest {

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
  CityService cityService;

  @Test
  void getAllCities_whenNoAuth_shouldReturn200WithPagedList() throws Exception {
    CityResponse response = new CityResponse(1, 20, "São Paulo");
    PagedResponse<CityResponse> pagedResponse = new PagedResponse<>(List.of(response), 0, 20, 1, 1);
    BDDMockito.given(cityService.findAll(0, 20)).willReturn(pagedResponse);

    mockMvc.perform(get(CityController.PATH).param("page", "0").param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items[0].id").value(1))
        .andExpect(jsonPath("$.items[0].name").value("São Paulo"))
        .andExpect(jsonPath("$.totalElements").value(1));
    mockMvc.perform(get(CityController.PATH).param("page", "0").param("size", "20"))
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().exists("Link"));
  }

  @Test
  void getCityById_whenFound_shouldReturn200() throws Exception {
    CityResponse response = new CityResponse(1, 20, "São Paulo");
    BDDMockito.given(cityService.findById(1)).willReturn(response);

    mockMvc.perform(get(CityController.PATH + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.stateId").value(20))
        .andExpect(jsonPath("$.name").value("São Paulo"));
  }

  @Test
  void getCityById_whenNotFound_shouldReturn404() throws Exception {
    BDDMockito.given(cityService.findById(999)).willThrow(new CityNotFoundException(999));

    mockMvc.perform(get(CityController.PATH + "/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void createCity_whenValidRequestAndEditor_shouldReturn201() throws Exception {
    CreateCityRequest request = new CreateCityRequest(20, "São Paulo");
    CityResponse response = new CityResponse(1, 20, "São Paulo");
    BDDMockito.given(cityService.create(any(CreateCityRequest.class))).willReturn(response);

    mockMvc.perform(post(CityController.PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().exists("Location"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("São Paulo"));
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void createCity_whenBlankName_shouldReturn400() throws Exception {
    CreateCityRequest request = new CreateCityRequest(20, "");

    mockMvc.perform(post(CityController.PATH)
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
  void updateCity_whenValidRequestAndEditor_shouldReturn200() throws Exception {
    UpdateCityRequest request = new UpdateCityRequest(20, "São Paulo Updated");
    CityResponse response = new CityResponse(1, 20, "São Paulo Updated");
    BDDMockito.given(cityService.update(anyInt(), any(UpdateCityRequest.class))).willReturn(response);

    mockMvc.perform(put(CityController.PATH + "/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("São Paulo Updated"));
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void updateCity_whenNotFoundAndEditor_shouldReturn404() throws Exception {
    UpdateCityRequest request = new UpdateCityRequest(20, "São Paulo");
    BDDMockito.given(cityService.update(anyInt(), any(UpdateCityRequest.class)))
        .willThrow(new CityNotFoundException(999));

    mockMvc.perform(put(CityController.PATH + "/999")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void deleteCity_whenFoundAndEditor_shouldReturn204() throws Exception {
    BDDMockito.willDoNothing()
        .given(cityService)
        .delete(anyInt());

    mockMvc.perform(delete(CityController.PATH + "/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void deleteCity_whenNotFoundAndEditor_shouldReturn404() throws Exception {
    BDDMockito.willThrow(new CityNotFoundException(999))
        .given(cityService)
        .delete(anyInt());

    mockMvc.perform(delete(CityController.PATH + "/999"))
        .andExpect(status().isNotFound());
  }
}
