package com.learning.postgres.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class UserService {

  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  List<Model> findAll() {
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  Model findById(Long id) {
    return userRepository.findById(id);
  }

  @Transactional
  void insert(Model model) {
    userRepository.insert(model);
  }

  @Transactional
  void insertAll(List<Model> list) {
    userRepository.insertAll(list);
  }

  @Transactional
  void deleteById(Long id) {
    userRepository.deleteById(id);
  }
}
