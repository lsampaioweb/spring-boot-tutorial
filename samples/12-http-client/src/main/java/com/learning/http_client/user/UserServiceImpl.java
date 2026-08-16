package com.learning.http_client.user;

import java.util.Objects;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.learning.http_client.config.ExternalApiProperties;

import jakarta.annotation.PostConstruct;

@Service
class UserServiceImpl implements UserService {

  private static final String ID_PARAMETER = "/{id}";
  private final RestClient.Builder restClientBuilder;
  private final ExternalApiProperties apiProperties;
  private final UserDtoMapper mapper;
  private RestClient restClient;

  UserServiceImpl(RestClient.Builder restClientBuilder, ExternalApiProperties apiProperties, UserDtoMapper mapper) {
    this.restClientBuilder = restClientBuilder;
    this.apiProperties = apiProperties;
    this.mapper = mapper;
  }

  @PostConstruct
  private void init() {
    this.restClient = restClientBuilder
        .baseUrl(apiProperties.users())
        .build();
  }

  @Override
  public PagedModel<EntityModel<UserResponse>> findAll(int page, int size, String sort) {
    PagedModel<EntityModel<User>> users = restClient
        .get()
        .uri(Objects.requireNonNull(getPagingAndSortingUrl(page, size, sort)))
        .retrieve()
        .body(new ParameterizedTypeReference<PagedModel<EntityModel<User>>>() {
        });

    if (users == null) {
      return PagedModel.empty();
    }

    return PagedModel.of(
        users.getContent().stream()
            .map(entityModel -> {
              UserResponse content = mapper.toResponse(Objects.requireNonNull(entityModel.getContent()));
              Links links = entityModel.getLinks();
              return EntityModel.of(content, links);
            })
            .toList(),
        users.getMetadata(),
        users.getLinks());
  }

  @Override
  public Optional<UserResponse> findById(Integer id) {
    User user = restClient
        .get()
        .uri(ID_PARAMETER, id)
        .retrieve()
        .body(User.class);

    return Optional.ofNullable(user).map(mapper::toResponse);
  }

  @Override
  public UserResponse create(UserRequest request) {
    User user = restClient
        .post()
        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
        .body(mapper.toEntity(Objects.requireNonNull(request)))
        .retrieve()
        .body(User.class);

    return mapper.toResponse(Objects.requireNonNull(user));
  }

  @Override
  public Optional<UserResponse> update(Integer id, UserRequest request) {
    User updatedUser = restClient
        .put()
        .uri(ID_PARAMETER, id)
        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
        .body(mapper.toEntity(Objects.requireNonNull(request)))
        .retrieve()
        .body(User.class);

    return Optional.ofNullable(updatedUser).map(mapper::toResponse);
  }

  @Override
  public boolean delete(Integer id) {
    HttpStatusCode status = restClient
        .delete()
        .uri(ID_PARAMETER, id)
        .retrieve()
        .toBodilessEntity()
        .getStatusCode();

    return ((status == HttpStatus.OK) || (status == HttpStatus.NO_CONTENT));
  }

  private String getPagingAndSortingUrl(int page, int size, String sort) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiProperties.users())
        .queryParam("page", Math.max(page, 0))
        .queryParam("size", Math.max(size, 1));

    if ((sort != null) && !sort.isBlank()) {
      builder.queryParam("sort", sort);
    }

    return builder.toUriString();
  }
}
