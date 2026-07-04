package com.learning.postgres.account;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
@ActiveProfiles("test")
class AccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AccountService accountService;

  @Test
  void findById_whenAccountExists_shouldReturn200() throws Exception {
    AccountResponse response = new AccountResponse(1L, "Alice", new BigDecimal("1000.00"));

    given(accountService.findById(1L)).willReturn(response);

    mockMvc.perform(get("/api/v1/accounts/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.ownerName").value("Alice"));
  }

  @Test
  void transfer_whenValidRequest_shouldReturn201() throws Exception {
    TransferResponse response = new TransferResponse(
        1L,
        2L,
        new BigDecimal("100.00"),
        new BigDecimal("900.00"),
        new BigDecimal("600.00"));

    String payload = """
        {
          "fromAccountId": 1,
          "toAccountId": 2,
          "amount": 100.00
        }
        """;

    given(accountService.transfer(org.mockito.ArgumentMatchers.any())).willReturn(response);

    mockMvc.perform(post("/api/v1/accounts/transfer")
        .contentType("application/json")
        .content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.fromAccountId").value(1L))
        .andExpect(jsonPath("$.toAccountId").value(2L));
  }

  @Test
  void transfer_whenInvalidRequest_shouldReturn400() throws Exception {
    String payload = """
        {
          "fromAccountId": 1,
          "toAccountId": 2,
          "amount": 0
        }
        """;

    mockMvc.perform(post("/api/v1/accounts/transfer")
        .contentType("application/json")
        .content(payload))
        .andExpect(status().isBadRequest());
  }
}
