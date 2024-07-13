package com.learning.http_client.user;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

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

  PagedModel<EntityModel<User>> findAll(Pageable pageable) {
    return restClient
        .get()
        .uri(getPagingAndSortingUrl(pageable))
        .retrieve()
        .body(new ParameterizedTypeReference<PagedModel<EntityModel<User>>>() {
        });
  }

  Optional<User> findById(Integer id) {
    User user = restClient
        .get()
        .uri("/{id}", id)
        .retrieve()
        .body(User.class);

    return Optional.ofNullable(user);
  }

  User create(User user) {
    return restClient
        .post()
        .contentType(MediaType.APPLICATION_JSON)
        .body(user)
        .retrieve()
        .body(User.class);
  }

  Optional<User> update(Integer id, User user) {
    User updatedUser = restClient
        .put()
        .uri("/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .body(user)
        .retrieve()
        .body(User.class);

    return Optional.ofNullable(updatedUser);
  }

  boolean delete(Integer id) {
    HttpStatusCode status = restClient
        .delete()
        .uri("/{id}", id)
        .retrieve()
        .toBodilessEntity()
        .getStatusCode();

    return ((status == HttpStatus.OK) || (status == HttpStatus.NO_CONTENT));
  }

  private String getPagingAndSortingUrl(Pageable pageable) {
    return UriComponentsBuilder.fromHttpUrl(usersUrl)
        .queryParam("page", pageable.getPageNumber())
        .queryParam("size", pageable.getPageSize())
        .queryParam("sort", formatSort(pageable.getSort()))
        .toUriString();
  }

  private String formatSort(Sort sort) {
    return sort.stream()
        .map(order -> order.getProperty() + "," + order.getDirection())
        .collect(Collectors.joining(","));
  }
}
