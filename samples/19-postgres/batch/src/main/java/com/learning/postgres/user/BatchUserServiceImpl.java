package com.learning.postgres.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.postgres.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

/**
 * Batch user service implementation with bulk operations.
 */
@Slf4j
@Service
class BatchUserServiceImpl implements BatchUserService {

  private static final String LOG_USER_BATCH_INSERTING = "log.user.batch.inserting";

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final LogMessages logMessages;

  BatchUserServiceImpl(UserRepository userRepository, UserMapper userMapper, LogMessages logMessages) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.logMessages = logMessages;
  }

  @Override
  @Transactional
  public BatchOperationResponse batchCreate(BatchCreateUserRequest request) {
    List<User> users = request.users().stream()
        .map(userMapper::toEntity)
        .toList();

    log.info(logMessages.get(LOG_USER_BATCH_INSERTING, users.size()));

    int[] rowsAffected = userRepository.batchInsert(users);

    List<UserResponse> createdUsers = new ArrayList<>();
    List<UserResponse> failedUsers = new ArrayList<>();

    for (int i = 0; i < rowsAffected.length; i++) {
      if (rowsAffected[i] > 0) {
        createdUsers.add(userMapper.toResponse(users.get(i)));
      } else {
        failedUsers.add(userMapper.toResponse(users.get(i)));
      }
    }

    return new BatchOperationResponse(
        request.users().size(),
        createdUsers.size(),
        createdUsers,
        failedUsers);
  }
}
