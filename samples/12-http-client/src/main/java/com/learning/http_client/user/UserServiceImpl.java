package com.learning.http_client.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper mapper;

  UserServiceImpl(UserRepository userRepository, UserMapper mapper) {
    this.userRepository = userRepository;
    this.mapper = mapper;
  }

  @Override
  public Page<UserResponse> findAll(Pageable pageable) {
    return userRepository.findAll(pageable).map(mapper::toResponse);
  }

  @Override
  public Optional<UserResponse> findById(Long id) {
    return userRepository.findById(id).map(mapper::toResponse);
  }

  @Override
  public UserResponse create(UserRequest request) {
    User created = userRepository.create(mapper.toEntity(request));

    return mapper.toResponse(created);
  }

  @Override
  public Optional<UserResponse> update(Long id, UserRequest request) {
    return userRepository.update(id, mapper.toEntity(request)).map(mapper::toResponse);
  }

  @Override
  public boolean delete(Long id) {
    return userRepository.delete(id);
  }
}
