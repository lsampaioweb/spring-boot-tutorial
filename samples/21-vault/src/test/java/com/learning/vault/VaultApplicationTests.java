package com.learning.vault;

import com.learning.vault.secret.VaultSecretRegistry;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class VaultApplicationTests {

  @MockitoBean
  VaultSecretRegistry vaultSecretRegistry;

  @Test
  void contextLoads() {
  }
}
