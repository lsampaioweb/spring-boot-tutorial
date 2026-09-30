package com.learning.exception_handling.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class UserServiceImpl implements UserService {

  private final UserMapper mapper;
  private final List<User> users = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong();

  UserServiceImpl(UserMapper mapper) {
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
  public Page<UserResponse> findAll(Pageable pageable) {
    List<User> sorted = getSortedUsers(users, pageable.getSort());
    List<UserResponse> content = slice(sorted, pageable).stream().map(mapper::toResponse).toList();

    return new PageImpl<>(content, pageable, users.size());
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

    User created = new User(idCounter.incrementAndGet(), entity.name(), entity.email());
    users.add(created);

    return mapper.toResponse(created);
  }

  @Override
  @Transactional
  public UserResponse update(Long id, UserRequest request) {
    User entity = findEntityById(id);
    User updated = new User(entity.id(), request.name(), request.email());

    users.remove(entity);
    users.add(updated);

    return mapper.toResponse(updated);
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
    return u -> u.id().equals(id);
  }

  private Predicate<? super User> hasSameIdentity(User entity) {
    return u -> u.name().equals(entity.name()) && u.email().equals(entity.email());
  }

  private List<User> getSortedUsers(List<User> users, Sort sort) {
    if (sort == null || sort.isUnsorted()) {
      return users;
    }

    List<User> sortedUsers = new ArrayList<>(users);

    for (Sort.Order order : sort) {
      Comparator<User> comparator = switch (order.getProperty()) {
        case "id" -> (left, right) -> left.id().compareTo(right.id());
        case "name" -> (left, right) -> left.name().compareTo(right.name());
        case "email" -> (left, right) -> left.email().compareTo(right.email());
        default -> throw new IllegalArgumentException("error.sort.property.invalid");
      };

      if (order.isDescending()) {
        comparator = comparator.reversed();
      }

      sortedUsers.sort(comparator);
    }

    return sortedUsers;
  }

  private List<User> slice(List<User> items, Pageable pageable) {
    int fromIndex = (int) pageable.getOffset();

    if (fromIndex >= items.size()) {
      return List.of();
    }

    int toIndex = Math.min(fromIndex + pageable.getPageSize(), items.size());

    return items.subList(fromIndex, toIndex);
  }
}
