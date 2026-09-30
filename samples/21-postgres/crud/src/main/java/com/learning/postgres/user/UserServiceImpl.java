package com.learning.postgres.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.postgres.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

/**
 * User service implementation with business logic.
 */
@Slf4j
@Service
class UserServiceImpl implements UserService {

  private static final String LOG_USER_INSERTING = "log.user.inserting";
  private static final String LOG_USER_UPDATING = "log.user.updating";
  private static final String LOG_USER_DELETING = "log.user.deleting";

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final LogMessages logMessages;

  UserServiceImpl(UserRepository userRepository, UserMapper userMapper, LogMessages logMessages) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.logMessages = logMessages;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserResponse> findAll(Pageable pageable) {
    return userRepository.findAll(pageable).map(userMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findById(Long id) {
    return userRepository.findById(id)
        .map(userMapper::toResponse)
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  @Override
  @Transactional
  public UserResponse create(CreateUserRequest request) {
    var user = userMapper.toEntity(request);

    log.info(logMessages.get(LOG_USER_INSERTING));
    User created = userRepository.insert(user);

    return userMapper.toResponse(created);
  }

  @Override
  @Transactional
  public UserResponse update(Long id, UpdateUserRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    User updatedUser = userMapper.updateEntity(request, user);

    log.info(logMessages.get(LOG_USER_UPDATING, updatedUser.id()));
    userRepository.update(updatedUser);

    return userMapper.toResponse(updatedUser);
  }

  @Override
  @Transactional
  public int delete(Long id) {
    userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    log.info(logMessages.get(LOG_USER_DELETING, id));
    return userRepository.deleteById(id);
  }
}
