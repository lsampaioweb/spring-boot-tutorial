package com.learning.http_client.user;

import java.util.List;

import org.springframework.http.HttpStatusCode;
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
@RequestMapping("http/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("")
  List<User> findAll() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  User findById(@PathVariable Integer id) {
    return userService.findById(id);
  }

  @PostMapping
  User create(@RequestBody User user) {
    return userService.create(user);
  }

  @PutMapping("/{id}")
  User update(@PathVariable Integer id, @RequestBody User user) {
    return userService.update(id, user);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable Integer id) {
    HttpStatusCode status = userService.delete(id).getStatusCode();

    if ((status != null) && (status.is2xxSuccessful())) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
