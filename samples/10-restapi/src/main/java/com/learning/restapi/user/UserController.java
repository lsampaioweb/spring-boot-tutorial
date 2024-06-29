package com.learning.restapi.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private List<User> users = new ArrayList<>();

  public UserController() {
    users.add(new User(1L, "user-01", "user-01@example.com"));
    users.add(new User(2L, "user-02", "user-02@example.com"));
  }

  @GetMapping
  public List<User> getUsers() {
    return users;
  }

  @GetMapping("/{id}")
  public User getUserById(@PathVariable Long id) {
    User user = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

    return user;
  }

  @PostMapping
  public User createUser(@RequestBody User user) {
    users.add(user);

    return user;
  }

  @PutMapping("/{id}")
  public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
    User user = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

    if (user != null) {
      user.setName(userDetails.getName());
      user.setEmail(userDetails.getEmail());
    }

    return user;
  }

  @DeleteMapping("/{id}")
  public String deleteUser(@PathVariable Long id) {
    users.removeIf(u -> u.getId().equals(id));

    return String.format("User with ID '%d' has been deleted.", id);
  }

}
