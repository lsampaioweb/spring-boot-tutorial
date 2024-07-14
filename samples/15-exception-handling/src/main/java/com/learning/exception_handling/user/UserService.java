package com.learning.exception_handling.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
class UserService {

  private List<User> users = new ArrayList<>();
  private AtomicLong idCounter = new AtomicLong();

  public UserService() {
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

  List<User> findAll() {
    return users;
  }

  User findById(Long id) {
    Optional<User> entity = users.stream().filter(getById(id)).findFirst();

    if (entity.isPresent()) {
      return entity.get();
    } else {
      throw new UserNotFoundException(id);
    }
  }

  User create(User entity) {
    boolean entityExists = users.stream().anyMatch(equals(entity));

    if (entityExists) {
      throw new UserAlreadyExistsException(entity);
    } else {
      entity.setId(idCounter.incrementAndGet());

      users.add(entity);

      return entity;
    }
  }

  User update(Long id, User entityDetails) {
    User entity = findById(id);

    entity.setName(entityDetails.getName());
    entity.setEmail(entityDetails.getEmail());

    return entity;
  }

  boolean delete(Long id) {
    User entity = findById(id);

    return users.remove(entity);
  }

  private Predicate<? super User> getById(Long id) {
    return u -> u.getId().equals(id);
  }

  private Predicate<? super User> equals(User entity) {
    return u -> u.getName().equals(entity.getName()) &&
        u.getEmail().equals(entity.getEmail());
  }

}
