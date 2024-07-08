package com.learning.restapi.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
class UserService {

  private List<User> users = new ArrayList<>();

  public UserService() {
    users.add(new User(1, "user-01", "user-01@example.com"));
    users.add(new User(2, "user-02", "user-02@example.com"));
    users.add(new User(3, "user-03", "user-03@example.com"));
    users.add(new User(4, "user-04", "user-04@example.com"));
    users.add(new User(5, "user-05", "user-05@example.com"));
    users.add(new User(6, "user-06", "user-06@example.com"));
    users.add(new User(7, "user-07", "user-07@example.com"));
    users.add(new User(8, "user-08", "user-08@example.com"));
    users.add(new User(9, "user-09", "user-09@example.com"));
    users.add(new User(10, "user-10", "user-10@example.com"));
  }

  List<User> findAll() {
    return users;
  }

  Page<User> paginateAndSort(Pageable pageable) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    Sort sort = pageable.getSort();

    List<User> paginatedUsers = getPaginatedList(users, pageNumber, pageSize);
    List<User> sortedPaginatedUsers = getSortedUsers(paginatedUsers, sort);

    return new PageImpl<>(sortedPaginatedUsers, PageRequest.of(pageNumber, pageSize, sort), users.size());
  }

  private List<User> getPaginatedList(List<User> users, int pageNumber, int pageSize) {
    int fromIndex = pageNumber * pageSize;

    if (users.size() < fromIndex) {
      return Collections.emptyList();
    } else {
      int toIndex = Math.min(fromIndex + pageSize, users.size());

      return users.subList(fromIndex, toIndex);
    }
  }

  private List<User> getSortedUsers(List<User> users, Sort sort) {
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

  Optional<User> findById(Integer id) {
    return users.stream().filter(getById(id)).findFirst();
  }

  User create(User user) {
    int newId = users.isEmpty() ? 1 : users.getLast().getId() + 1;

    user.setId(newId);
    users.add(user);

    return user;
  }

  Optional<User> update(Integer id, User userDetails) {
    return users.stream().filter(getById(id)).findFirst()
        .map(user -> {
          user.setName(userDetails.getName());
          user.setEmail(userDetails.getEmail());

          return user;
        });
  }

  boolean delete(Integer id) {
    return users.removeIf(getById(id));
  }

  private Predicate<? super User> getById(Integer id) {
    return u -> u.getId().equals(id);
  }

}
