package com.learning.restapi.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

  Page<User> findAll(Pageable pageable) {
    List<User> paginatedUsers = getPaginatedList(users, pageable);
    List<User> sortedPaginatedUsers = getSortedUsers(paginatedUsers, pageable);

    return new PageImpl<>(sortedPaginatedUsers, pageable, users.size());
  }

  Optional<User> findById(Long id) {
    return users.stream().filter(getById(id)).findFirst();
  }

  User create(User user) {
    user.setId(idCounter.incrementAndGet());

    users.add(user);

    return user;
  }

  Optional<User> update(Long id, User userDetails) {
    return users.stream().filter(getById(id)).findFirst()
        .map(user -> {
          user.setName(userDetails.getName());
          user.setEmail(userDetails.getEmail());

          return user;
        });
  }

  boolean delete(Long id) {
    return users.removeIf(getById(id));
  }

  private Predicate<? super User> getById(Long id) {
    return u -> u.getId().equals(id);
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
      Comparator<User> comparator;

      switch (order.getProperty()) {
        case "id":
          comparator = Comparator.comparing(User::getId);
          break;
        case "name":
          comparator = Comparator.comparing(User::getName);
          break;
        case "email":
          comparator = Comparator.comparing(User::getEmail);
          break;
        default:
          throw new IllegalArgumentException("Invalid sort property: " + order.getProperty());
      }

      if (order.getDirection() == Sort.Direction.DESC) {
        comparator = comparator.reversed();
      }

      sortedUsers = sortedUsers.stream()
          .sorted(comparator)
          .collect(Collectors.toList());
    }

    return sortedUsers;
  }

}
