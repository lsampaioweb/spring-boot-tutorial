package com.learning.restapi.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.context.MessageSource;
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
  public PagedModel<EntityModel<UserResponse>> findAllPaged(int page, int size, String sort) {
    int normalizedPage = Math.max(page, 0);
    int normalizedSize = Math.max(size, 1);
    List<SortOption> sortOptions = parseSortOptions(sort);

    List<User> sortedUsers = getSortedUsers(users, sortOptions);
    List<User> paginatedUsers = getPaginatedList(sortedUsers, normalizedPage, normalizedSize);
    List<EntityModel<UserResponse>> content = paginatedUsers.stream()
        .map(this::toResponse)
        .map(EntityModel::of)
        .toList();

    int totalItems = users.size();
    long totalPages = (totalItems == 0) ? 0 : ((totalItems + (long) normalizedSize - 1) / normalizedSize);

    PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
        normalizedSize,
        normalizedPage,
        totalItems,
        totalPages);

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

  private List<User> getPaginatedList(List<User> users, int page, int size) {
    int fromIndex = page * size;

    if (users.size() < fromIndex) {
      return Collections.emptyList();
    } else {
      int toIndex = Math.min(fromIndex + size, users.size());

      return users.subList(fromIndex, toIndex);
    }
  }

  private List<User> getSortedUsers(List<User> users, List<SortOption> sortOptions) {
    if (sortOptions.isEmpty()) {
      return users;
    }

    List<User> sortedUsers = new ArrayList<>(users);

    for (SortOption option : sortOptions) {
      Comparator<User> comparator = switch (option.property()) {
        case "id" -> Comparator.comparing(User::id);
        case "name" -> Comparator.comparing(User::name);
        case "email" -> Comparator.comparing(User::email);
        default -> throw new IllegalArgumentException(getMessage(ERROR_SORT_PROPERTY_INVALID, option.property()));
      };

      if (option.descending()) {
        comparator = comparator.reversed();
      }

      sortedUsers = sortedUsers.stream().sorted(comparator).toList();
    }

    return sortedUsers;
  }

  private String getMessage(String key, Object... args) {
    return messageSource.getMessage(key, args, Locale.ENGLISH);
  }

  private List<SortOption> parseSortOptions(String sort) {
    if ((sort == null) || sort.isBlank()) {
      return List.of();
    }

    String[] tokens = sort.split(",");

    if ((tokens.length % 2) != 0) {
      throw new IllegalArgumentException(getMessage(ERROR_SORT_PROPERTY_INVALID, sort));
    }

    List<SortOption> options = new ArrayList<>();

    for (int i = 0; i < tokens.length; i += 2) {
      String property = tokens[i].trim();
      String direction = tokens[i + 1].trim();
      boolean descending = "desc".equalsIgnoreCase(direction);

      options.add(new SortOption(property, descending));
    }

    return options;
  }

  private record SortOption(String property, boolean descending) {
  }
}
