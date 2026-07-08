package com.learning.geography.country;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/countries")
@Tag(name = "Countries")
class CountryController {

  private final CountryService countryService;

  CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @GetMapping
  public ResponseEntity<List<CountryResponse>> getAllCountries() {
    return ResponseEntity.ok(countryService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CountryResponse> getCountryById(@PathVariable Long id) {
    return ResponseEntity.ok(countryService.findById(id));
  }

  @PostMapping
  public ResponseEntity<CountryResponse> createCountry(@Valid @RequestBody CreateCountryRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(countryService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CountryResponse> updateCountry(
      @PathVariable Long id,
      @Valid @RequestBody UpdateCountryRequest request) {
    return ResponseEntity.ok(countryService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
    countryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
