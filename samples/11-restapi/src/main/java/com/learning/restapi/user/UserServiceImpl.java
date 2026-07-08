package com.learning.restapi.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
class UserServiceImpl implements UserService {

  private static final String ERROR_SORT_PROPERTY_INVALID = "error.sort.property.invalid";

  private final List<User> users = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong();
  private final MessageSource messageSource;

  UserServiceImpl(MessageSource messageSource) {
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
    List<User> paginatedUsers = getPaginatedList(users, pageable);
    List<User> sortedPaginatedUsers = getSortedUsers(paginatedUsers, pageable);
    List<UserResponse> responses = sortedPaginatedUsers.stream().map(this::toResponse).toList();

    return new PageImpl<>(responses, Objects.requireNonNull(pageable), users.size());
  }

  @Override
  public PagedModel<EntityModel<UserResponse>> findAllPaged(Pageable pageable) {
    Page<UserResponse> usersPage = findAll(pageable);
    List<EntityModel<UserResponse>> content = usersPage.getContent()
        .stream()
        .map(EntityModel::of)
        .toList();

    PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
        usersPage.getSize(),
        usersPage.getNumber(),
        usersPage.getTotalElements(),
        usersPage.getTotalPages());

    return PagedModel.of(content, metadata);
  }

  @Override
  public Optional<UserResponse> findById(Long id) {
    return users.stream().filter(getById(id)).findFirst().map(this::toResponse);
  }

  @Override
  public UserResponse create(UserRequest request) {
    User user = new User(idCounter.incrementAndGet(), request.name(), request.email());
    users.add(user);

    return toResponse(user);
  }

  @Override
  public Optional<UserResponse> update(Long id, UserRequest request) {
    return users.stream().filter(getById(id)).findFirst()
        .map(existing -> {
          User updated = new User(existing.id(), request.name(), request.email());
          users.remove(existing);
          users.add(updated);

          return toResponse(updated);
        });
  }

  @Override
  public boolean delete(Long id) {
    return users.removeIf(getById(id));
  }

  private UserResponse toResponse(User user) {
    return new UserResponse(user.id(), user.name(), user.email());
  }

  private Predicate<? super User> getById(Long id) {
    return u -> u.id().equals(id);
  }

  private List<User> getPaginatedList(List<User> users, Pageable pageable) {
    int fromIndex = pageable.getPageNumber() * pageable.getPageSize();

    if (users.size() < fromIndex) {
      return Collections.emptyList();
    } else {
      int toIndex = Math.min(fromIndex + pageable.getPageSize(), users.size());

      return users.subList(fromIndex, toIndex);
    }
  }

  private List<User> getSortedUsers(List<User> users, Pageable pageable) {
    Sort sort = pageable.getSort();

    if (!sort.isSorted()) {
      return users;
    }

    List<User> sortedUsers = new ArrayList<>(users);

    for (Sort.Order order : sort) {
      Comparator<User> comparator = switch (order.getProperty()) {
        case "id" -> Comparator.comparing(User::id);
        case "name" -> Comparator.comparing(User::name);
        case "email" -> Comparator.comparing(User::email);
        default -> throw new IllegalArgumentException(getMessage(ERROR_SORT_PROPERTY_INVALID, order.getProperty()));
      };

      if (order.getDirection() == Sort.Direction.DESC) {
        comparator = comparator.reversed();
      }

      sortedUsers = sortedUsers.stream().sorted(comparator).toList();
    }

    return sortedUsers;
  }

  private String getMessage(String key, Object... args) {
    return messageSource.getMessage(key, args, Locale.ENGLISH);
  }
}
