package com.learning.postgres.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learning.postgres.i18n.LogMessages;

@ExtendWith(MockitoExtension.class)
class BatchUserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private LogMessages logMessages;

  private BatchUserServiceImpl batchUserService;

  @BeforeEach
  void setUp() {
    batchUserService = new BatchUserServiceImpl(userRepository, new UserMapper(), logMessages);
  }

  @Test
  void batchCreate_whenAllRowsAffected_shouldReturnAllCreated() {
    BatchCreateUserRequest request = new BatchCreateUserRequest(
        List.of(new BatchCreateUserRequest.UserData("John Doe", "john@example.com")));

    when(userRepository.batchInsert(anyList())).thenReturn(new int[] { 1 });

    BatchOperationResponse response = batchUserService.batchCreate(request);

    assertThat(response.totalRequested()).isEqualTo(1);
    assertThat(response.totalProcessed()).isEqualTo(1);
    assertThat(response.createdUsers()).hasSize(1);
    assertThat(response.failedUsers()).isEmpty();
  }
}
