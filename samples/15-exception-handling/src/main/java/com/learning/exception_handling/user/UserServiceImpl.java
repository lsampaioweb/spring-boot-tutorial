package com.learning.exception_handling.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class UserServiceImpl implements UserService {

  private final UserDtoMapper mapper;
  private List<User> users = new ArrayList<>();
  private AtomicLong idCounter = new AtomicLong();

  UserServiceImpl(UserDtoMapper mapper) {
    this.mapper = mapper;

    users.add(new User(idCounter.incrementAndGet(), "user-01", "user-01@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-02", "user-02@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-03", "user-03@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-04", "user-04@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-05", "user-05@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-06", "user-06@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-07", "user-07@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-08", "user-08@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-09", "user-09@example.com"));
    users.add(new User(idCounter.incrementAndGet(), "user-10", "user-10@example.com"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> findAll() {
    return users.stream().map(mapper::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findById(Long id) {
    return mapper.toResponse(findEntityById(id));
  }

  @Override
  @Transactional
  public UserResponse create(UserRequest request) {
    User entity = mapper.toEntity(request);
    boolean entityExists = users.stream().anyMatch(hasSameIdentity(entity));

    if (entityExists) {
      throw new UserAlreadyExistsException(entity);
    }

    entity.setId(idCounter.incrementAndGet());
    users.add(entity);

    return mapper.toResponse(entity);
  }

  @Override
  @Transactional
  public UserResponse update(Long id, UserRequest request) {
    User entity = findEntityById(id);

    entity.setName(request.name());
    entity.setEmail(request.email());

    return mapper.toResponse(entity);
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    User entity = findEntityById(id);

    return users.remove(entity);
  }

  private User findEntityById(Long id) {
    Optional<User> entity = users.stream().filter(getById(id)).findFirst();

    if (entity.isPresent()) {
      return entity.get();
    }

    throw new UserNotFoundException(id);
  }

  private Predicate<? super User> getById(Long id) {
    return u -> u.getId().equals(id);
  }

  private Predicate<? super User> hasSameIdentity(User entity) {
    return u -> u.getName().equals(entity.getName()) &&
        u.getEmail().equals(entity.getEmail());
  }

}
