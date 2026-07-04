package com.learning.postgres.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * User service implementation with business logic.
 */
@Slf4j
@Service
class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> findAll() {
    return userRepository.findAll().stream().map(userMapper::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findById(Long id) {
    var user = userRepository.findById(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }
    return userMapper.toResponse(user);
  }

  @Override
  @Transactional
  public UserResponse create(CreateUserRequest request) {
    var user = userMapper.toEntity(request);
    userRepository.insert(user);

    return userMapper.toResponse(user);
  }

  @Override
  @Transactional
  public UserResponse update(Long id, UpdateUserRequest request) {
    var user = userRepository.findById(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }
    var updatedUser = userMapper.updateEntity(request, user);
    userRepository.update(updatedUser);

    return userMapper.toResponse(updatedUser);
  }

  @Override
  @Transactional
  public int delete(Long id) {
    var user = userRepository.findById(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }
    return userRepository.deleteById(id);
  }
}
