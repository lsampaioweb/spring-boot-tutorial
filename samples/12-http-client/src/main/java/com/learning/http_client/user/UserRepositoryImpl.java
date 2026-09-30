package com.learning.http_client.user;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Repository
class UserRepositoryImpl implements UserRepository {

  private static final String ID_PATH = "/{id}";

  private final RestClient usersRestClient;

  UserRepositoryImpl(RestClient usersRestClient) {
    this.usersRestClient = usersRestClient;
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    RestPage<User> page = usersRestClient
        .get()
        .uri(uriBuilder -> {
          uriBuilder.queryParam("page", pageable.getPageNumber())
              .queryParam("size", pageable.getPageSize());
          pageable.getSort().forEach(order -> uriBuilder.queryParam("sort",
              order.getProperty() + "," + order.getDirection().name().toLowerCase(Locale.ROOT)));
          return uriBuilder.build();
        })
        .retrieve()
        .body(new ParameterizedTypeReference<RestPage<User>>() {
        });

    if (page == null) {
      return Page.empty(pageable);
    }

    return page;
  }

  @Override
  public Optional<User> findById(Long id) {
    return usersRestClient
        .get()
        .uri(ID_PATH, id)
        .exchange((request, response) -> {
          if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
          }

          return Optional.ofNullable(response.bodyTo(User.class));
        });
  }

  @Override
  public User create(User user) {
    return usersRestClient
        .post()
        .contentType(MediaType.APPLICATION_JSON)
        .body(user)
        .retrieve()
        .body(User.class);
  }

  @Override
  public Optional<User> update(Long id, User user) {
    return usersRestClient
        .put()
        .uri(ID_PATH, id)
        .contentType(MediaType.APPLICATION_JSON)
        .body(user)
        .exchange((request, response) -> {
          if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
          }

          return Optional.ofNullable(response.bodyTo(User.class));
        });
  }

  @Override
  public boolean delete(Long id) {
    return Boolean.TRUE.equals(usersRestClient
        .delete()
        .uri(ID_PATH, id)
        .exchange((request, response) -> {
          if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return false;
          }

          return response.getStatusCode() == HttpStatus.NO_CONTENT
              || response.getStatusCode() == HttpStatus.OK;
        }));
  }

  static class RestPage<T> extends PageImpl<T> {

    @JsonCreator
    RestPage(
        @JsonProperty("content") List<T> content,
        @JsonProperty("number") int number,
        @JsonProperty("size") int size,
        @JsonProperty("totalElements") long totalElements) {
      super(content == null ? List.of() : content, PageRequest.of(number, Math.max(size, 1)), totalElements);
    }
  }
}
