package com.learning.postgres.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
class UserService {

  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  List<Model> findAll() {
    return userRepository.findAll();
  }

  Model findById(Long id) {
    return userRepository.findById(id);
  }

  void insert(Model model) {
    userRepository.insert(model);
  }

  void insertAll(List<Model> list) {
    userRepository.insertAll(list);
  }

  void deleteById(Long id) {
    userRepository.deleteById(id);
  }
}
