package com.learning.postgres.user;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<Model> getAllUsers() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  public Model getUserById(@PathVariable Long id) {
    return userService.findById(id);
  }

  // Create a single user.
  @PostMapping
  public void createUser(@RequestBody Model model) {
    userService.insert(model);
  }

  // Create multiple users (use a different path).
  @PostMapping("/batch")
  public void createUsers(@RequestBody List<Model> list) {
    userService.insertAll(list);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.deleteById(id);
  }
}
