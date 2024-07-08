package com.learning.restapi.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("")
  List<User> findAll() {
    return userService.findAll();
  }

  @GetMapping("/all")
  public Page<User> all(Pageable pageable) {
    return userService.paginateAndSort(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable Integer id) {
    Optional<User> user = userService.findById(id);

    return user.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user) {
    User createdUser = userService.create(user);

    return ResponseEntity.ok(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
    Optional<User> updatedUser = userService.update(id, user);

    return updatedUser.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    boolean userRemoved = userService.delete(id);

    if (userRemoved) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
