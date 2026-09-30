package com.learning.mapstruct.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
class UserServiceImpl implements UserService {

  private final List<User> users = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong();
  private final UserMapper userMapper;

  UserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;

    users.add(userMapper.toEntity(idCounter.incrementAndGet(), new UserRequest("user-01", "user-01@example.com")));
    users.add(userMapper.toEntity(idCounter.incrementAndGet(), new UserRequest("user-02", "user-02@example.com")));
    users.add(userMapper.toEntity(idCounter.incrementAndGet(), new UserRequest("user-03", "user-03@example.com")));
  }

  @Override
  public List<UserResponse> findAll() {
    return users.stream().map(userMapper::toResponse).toList();
  }

  @Override
  public UserResponse findById(Long id) {
    return users.stream().filter(getById(id)).findFirst().map(userMapper::toResponse).orElse(null);
  }

  @Override
  public UserResponse create(UserRequest request) {
    User draft = userMapper.toNewEntity(request);
    User user = new User(idCounter.incrementAndGet(), draft.name(), draft.email());
    users.add(user);

    return userMapper.toResponse(user);
  }

  @Override
  public UserResponse update(Long id, UserRequest request) {
    return users.stream().filter(getById(id)).findFirst()
        .map(existing -> {
          User updated = userMapper.toEntity(existing.id(), request);
          users.remove(existing);
          users.add(updated);

          return userMapper.toResponse(updated);
        })
        .orElse(null);
  }

  @Override
  public boolean delete(Long id) {
    return users.removeIf(getById(id));
  }

  private Predicate<? super User> getById(Long id) {
    return user -> user.id().equals(id);
  }
}
