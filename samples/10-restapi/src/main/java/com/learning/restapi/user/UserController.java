package com.learning.restapi.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
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
    users.add(new User(1, "user-01", "user-01@example.com"));
    users.add(new User(2, "user-02", "user-02@example.com"));
  }

  @GetMapping
  List<User> findAll() {
    return users;
  }

  @GetMapping("/{id}")
  User findById(@PathVariable Integer id) {
    User user = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

    return user;
  }

  @PostMapping
  User create(@RequestBody User user) {
    user.setId(users.getLast().getId() + 1);

    users.add(user);

    return user;
  }

  @PutMapping("/{id}")
  User update(@PathVariable Integer id, @RequestBody User userDetails) {
    User user = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

    if (user != null) {
      user.setName(userDetails.getName());
      user.setEmail(userDetails.getEmail());
    }

    return user;
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable Integer id) {
    boolean userRemoved = users.removeIf(u -> u.getId().equals(id));

    if (userRemoved) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
