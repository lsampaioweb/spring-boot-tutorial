package com.learning.postgres.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Batch user service implementation with bulk operations.
 */
@Service
class BatchUserServiceImpl implements BatchUserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  BatchUserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  @Transactional
  public BatchOperationResponse batchCreate(BatchCreateUserRequest request) {
    List<User> users = request.users().stream()
        .map(userMapper::toEntity)
        .toList();

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
