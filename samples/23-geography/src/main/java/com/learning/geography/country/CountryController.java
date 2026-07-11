package com.learning.geography.country;

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
@RequestMapping(CountryController.PATH)
@Tag(name = "Countries")
class CountryController {

  static final String PATH = "/api/v1/countries";

  private final CountryService countryService;
  private final PaginationConfigurationProperties paginationProperties;

  CountryController(CountryService countryService,
      PaginationConfigurationProperties paginationProperties) {
    this.countryService = countryService;
    this.paginationProperties = paginationProperties;
  }

  @GetMapping
  public ResponseEntity<PagedResponse<CountryResponse>> getAllCountries(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(required = false) @Min(1) @Max(100) Integer size) {
    int effectiveSize = size != null
        ? Math.min(size, paginationProperties.maxPageSize())
        : paginationProperties.defaultPageSize();

    PagedResponse<CountryResponse> response = countryService.findAll(page, effectiveSize);

    return ResponseEntity.ok()
        .headers(buildPaginationHeaders(response))
        .body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CountryResponse> getCountryById(@PathVariable Integer id) {
    return ResponseEntity.ok(countryService.findById(id));
  }

  @PostMapping
  public ResponseEntity<CountryResponse> createCountry(@Valid @RequestBody CreateCountryRequest request) {
    CountryResponse response = countryService.create(request);

    return ResponseEntity
        .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri())
        .body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CountryResponse> updateCountry(
      @PathVariable Integer id,
      @Valid @RequestBody UpdateCountryRequest request) {
    return ResponseEntity.ok(countryService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCountry(@PathVariable Integer id) {
    countryService.delete(id);

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
