package com.learning.http_client.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;

@Service
class UserService {

  private final RestClient.Builder restClientBuilder;
  private RestClient restClient;

  @Value("${external.api.users}")
  private String usersUrl;

  public UserService(RestClient.Builder restClientBuilder) {
    this.restClientBuilder = restClientBuilder;
  }

  @PostConstruct
  private void init() {
    this.restClient = restClientBuilder
        .baseUrl(usersUrl)
        .build();
  }

  public List<User> findAll() {
    return restClient
        .get()
        .retrieve()
        .body(new ParameterizedTypeReference<List<User>>() {
        });
  }

  public User findById(Integer id) {
    return restClient
        .get()
        .uri("/{id}", id)
        .retrieve()
        .body(User.class);
  }

  public User create(User user) {
    return restClient
        .post()
        .contentType(MediaType.APPLICATION_JSON)
        .body(user)
        .retrieve()
        .body(User.class);
  }

  public User update(Integer id, User user) {
    return restClient
        .put()
        .uri("/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .body(user)
        .retrieve()
        .body(User.class);
  }

  public ResponseEntity<Void> delete(Integer id) {
    return restClient
        .delete()
        .uri("/{id}", id)
        .retrieve()
        .toBodilessEntity();
  }
}
