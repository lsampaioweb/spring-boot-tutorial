package com.learning.exception_handling.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
class UserService {

  private final MessageSource messageSource;

  private List<User> users = new ArrayList<>();
  private AtomicLong idCounter = new AtomicLong();

  public UserService(MessageSource messageSource) {
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

  List<User> findAll() {
    return users;
  }

  User findById(Long id) {
    Optional<User> user = users.stream().filter(getById(id)).findFirst();

    if (user.isPresent()) {
      return user.get();
    } else {
      throw new NoSuchUserExistsException(getUserNotFoundMessage(id));
    }
  }

  User create(User user) {
    boolean userExists = users.stream().anyMatch(equals(user));

    if (userExists) {
      throw new UserAlreadyExistsException(getUserAlreadyExistsMessage(user));
    } else {
      user.setId(idCounter.incrementAndGet());

      users.add(user);

      return user;
    }
  }

  User update(Long id, User userDetails) {
    User user = findById(id);

    user.setName(userDetails.getName());
    user.setEmail(userDetails.getEmail());

    return user;
  }

  boolean delete(Long id) {
    User user = findById(id);

    return users.remove(user);
  }

  private Predicate<? super User> getById(Long id) {
    return u -> u.getId().equals(id);
  }

  private Predicate<? super User> equals(User user) {
    return u -> u.getName().equals(user.getName()) &&
        u.getEmail().equals(user.getEmail());
  }

  private Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }

  private String getUserNotFoundMessage(Long id) {
    return messageSource.getMessage("user.notfound", new Object[] { id }, getLocale());
  }

  private String getUserAlreadyExistsMessage(User user) {
    return messageSource.getMessage("user.exists", new Object[] { user.getName(), user.getEmail() }, getLocale());
  }

}
