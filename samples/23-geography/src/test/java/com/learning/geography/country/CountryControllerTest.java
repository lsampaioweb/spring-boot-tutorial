package com.learning.geography.country;

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
import com.learning.geography.i18n.LogMessages;

@WebMvcTest(CountryController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CountryControllerTest {

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
  CountryService countryService;

  @MockitoBean
  LogMessages logMessages;

  @Test
  void getAllCountries_whenNoAuth_shouldReturn200WithPagedList() throws Exception {
    CountryResponse response = new CountryResponse(1, "Brazil", "BR");
    PagedResponse<CountryResponse> pagedResponse = new PagedResponse<>(List.of(response), 0, 20, 1, 1);
    BDDMockito.given(countryService.findAll(0, 20)).willReturn(pagedResponse);

    mockMvc.perform(get(CountryController.PATH).param("page", "0").param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items[0].id").value(1))
        .andExpect(jsonPath("$.items[0].name").value("Brazil"))
        .andExpect(jsonPath("$.totalElements").value(1));
    mockMvc.perform(get(CountryController.PATH).param("page", "0").param("size", "20"))
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().exists("Link"));
  }

  @Test
  void getCountryById_whenFound_shouldReturn200() throws Exception {
    CountryResponse response = new CountryResponse(1, "Brazil", "BR");
    BDDMockito.given(countryService.findById(1)).willReturn(response);

    mockMvc.perform(get(CountryController.PATH + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Brazil"))
        .andExpect(jsonPath("$.isoCode").value("BR"));
  }

  @Test
  void getCountryById_whenNotFound_shouldReturn404() throws Exception {
    BDDMockito.given(countryService.findById(999)).willThrow(new CountryNotFoundException(999));

    mockMvc.perform(get(CountryController.PATH + "/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void createCountry_whenValidRequestAndEditor_shouldReturn201() throws Exception {
    CreateCountryRequest request = new CreateCountryRequest("Brazil", "BR");
    CountryResponse response = new CountryResponse(1, "Brazil", "BR");
    BDDMockito.given(countryService.create(any(CreateCountryRequest.class))).willReturn(response);

    mockMvc.perform(post(CountryController.PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().exists("Location"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Brazil"));
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void createCountry_whenBlankName_shouldReturn400() throws Exception {
    CreateCountryRequest request = new CreateCountryRequest("", "BR");

    mockMvc.perform(post(CountryController.PATH)
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
  void updateCountry_whenValidRequestAndEditor_shouldReturn200() throws Exception {
    UpdateCountryRequest request = new UpdateCountryRequest("Brazil Updated", "BU");
    CountryResponse response = new CountryResponse(1, "Brazil Updated", "BU");
    BDDMockito.given(countryService.update(anyInt(), any(UpdateCountryRequest.class))).willReturn(response);

    mockMvc.perform(put(CountryController.PATH + "/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Brazil Updated"));
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void updateCountry_whenNotFoundAndEditor_shouldReturn404() throws Exception {
    UpdateCountryRequest request = new UpdateCountryRequest("Brazil", "BR");
    BDDMockito.given(countryService.update(anyInt(), any(UpdateCountryRequest.class)))
        .willThrow(new CountryNotFoundException(999));

    mockMvc.perform(put(CountryController.PATH + "/999")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void deleteCountry_whenFoundAndEditor_shouldReturn204() throws Exception {
    BDDMockito.willDoNothing().given(countryService).delete(anyInt());

    mockMvc.perform(delete(CountryController.PATH + "/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void deleteCountry_whenNotFoundAndEditor_shouldReturn404() throws Exception {
    BDDMockito.willThrow(new CountryNotFoundException(999)).given(countryService).delete(anyInt());

    mockMvc.perform(delete(CountryController.PATH + "/999"))
        .andExpect(status().isNotFound());
  }
}
