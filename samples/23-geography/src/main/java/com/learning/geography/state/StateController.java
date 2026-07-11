package com.learning.geography.state;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.learning.geography.common.PagedResponse;
import com.learning.geography.common.PaginationConfigurationProperties;

@Validated
@RestController
@RequestMapping(StateController.PATH)
@Tag(name = "States")
class StateController {

  static final String PATH = "/api/v1/states";

  private final StateService stateService;
  private final PaginationConfigurationProperties paginationProperties;

  StateController(StateService stateService, PaginationConfigurationProperties paginationProperties) {
    this.stateService = stateService;
    this.paginationProperties = paginationProperties;
  }

  @GetMapping
  public ResponseEntity<PagedResponse<StateResponse>> getAllStates(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(required = false) @Min(1) @Max(100) Integer size) {
    int effectiveSize = size != null
        ? Math.min(size, paginationProperties.maxPageSize())
        : paginationProperties.defaultPageSize();

    PagedResponse<StateResponse> response = stateService.findAll(page, effectiveSize);

    return ResponseEntity.ok()
        .headers(buildPaginationHeaders(response))
        .body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StateResponse> getStateById(@PathVariable Integer id) {
    return ResponseEntity.ok(stateService.findById(id));
  }

  @PostMapping
  public ResponseEntity<StateResponse> createState(@Valid @RequestBody CreateStateRequest request) {
    StateResponse response = stateService.create(request);

    return ResponseEntity
        .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri())
        .body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<StateResponse> updateState(
      @PathVariable Integer id,
      @Valid @RequestBody UpdateStateRequest request) {
    return ResponseEntity.ok(stateService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteState(@PathVariable Integer id) {
    stateService.delete(id);
    return ResponseEntity.noContent().build();
  }

  private HttpHeaders buildPaginationHeaders(PagedResponse<?> response) {
    HttpHeaders headers = new HttpHeaders();
    List<String> links = new ArrayList<>();

    int totalPages = response.totalPages();
    int currentPage = response.page();
    int size = response.size();

    if (totalPages > 0) {
      links.add(buildLink(0, size, "first"));
      links.add(buildLink(totalPages - 1, size, "last"));
      if (currentPage > 0) {
        links.add(buildLink(currentPage - 1, size, "prev"));
      }
      if (currentPage + 1 < totalPages) {
        links.add(buildLink(currentPage + 1, size, "next"));
      }
    }

    if (!links.isEmpty()) {
      headers.add(HttpHeaders.LINK, String.join(", ", links));
    }

    return headers;
  }

  private String buildLink(int page, int size, String rel) {
    String uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .replaceQueryParam("page", page)
        .replaceQueryParam("size", size)
        .build()
        .toUriString();

    return "<" + uri + ">; rel=\"" + rel + "\"";
  }
}
