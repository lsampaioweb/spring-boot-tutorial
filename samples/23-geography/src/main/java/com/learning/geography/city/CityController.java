package com.learning.geography.city;

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
@RequestMapping("/api/v1/cities")
@Tag(name = "Cities")
class CityController {

  private final CityService cityService;

  CityController(CityService cityService) {
    this.cityService = cityService;
  }

  @GetMapping
  public ResponseEntity<List<CityResponse>> getAllCities() {
    return ResponseEntity.ok(cityService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CityResponse> getCityById(@PathVariable Long id) {
    return ResponseEntity.ok(cityService.findById(id));
  }

  @PostMapping
  public ResponseEntity<CityResponse> createCity(@Valid @RequestBody CreateCityRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cityService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CityResponse> updateCity(
      @PathVariable Long id,
      @Valid @RequestBody UpdateCityRequest request) {
    return ResponseEntity.ok(cityService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
    cityService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
