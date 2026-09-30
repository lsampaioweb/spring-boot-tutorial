package com.learning.validation.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
class UserServiceImpl implements UserService {

  private final List<User> users = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong();
  private final UserMapper userMapper;
  private final MessageSource messageSource;

  UserServiceImpl(UserMapper userMapper, MessageSource messageSource) {
    this.userMapper = userMapper;
    this.messageSource = messageSource;

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
  public Page<UserResponse> findAll(Pageable pageable) {
    List<User> sortedUsers = getSortedUsers(users, pageable.getSort());
    List<UserResponse> content = getPaginatedList(sortedUsers, pageable.getPageNumber(), pageable.getPageSize())
        .stream()
        .map(userMapper::toResponse)
        .toList();

    return new PageImpl<>(content, pageable, users.size());
  }

  @Override
  public UserResponse findById(Long id) {
    return users.stream().filter(getById(id)).findFirst().map(userMapper::toResponse).orElse(null);
  }

  @Override
  public UserResponse create(UserRequest request) {
    User user = new User(idCounter.incrementAndGet(), request.name(), request.email());
    users.add(user);

    return userMapper.toResponse(user);
  }

  @Override
  public UserResponse update(Long id, UserRequest request) {
    return users.stream().filter(getById(id)).findFirst()
        .map(existing -> {
          User updated = new User(existing.id(), request.name(), request.email());
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
    return u -> u.id().equals(id);
  }

  private List<User> getPaginatedList(List<User> users, int page, int size) {
    int fromIndex = page * size;

    if (users.size() < fromIndex) {
      return Collections.emptyList();
    }

    int toIndex = Math.min(fromIndex + size, users.size());

    return users.subList(fromIndex, toIndex);
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
        default -> throw new IllegalArgumentException(messageSource.getMessage(
            "error.sort.property.invalid",
            new Object[] { order.getProperty() },
            LocaleContextHolder.getLocale()));
      };

      if (order.isDescending()) {
        comparator = comparator.reversed();
      }

      sortedUsers.sort(comparator);
    }

    return sortedUsers;
  }
}
