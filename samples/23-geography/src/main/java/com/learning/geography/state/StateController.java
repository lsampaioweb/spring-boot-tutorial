package com.learning.geography.state;

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
@RequestMapping("/api/v1/states")
@Tag(name = "States")
class StateController {

  private final StateService stateService;

  StateController(StateService stateService) {
    this.stateService = stateService;
  }

  @GetMapping
  public ResponseEntity<List<StateResponse>> getAllStates() {
    return ResponseEntity.ok(stateService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<StateResponse> getStateById(@PathVariable Long id) {
    return ResponseEntity.ok(stateService.findById(id));
  }

  @PostMapping
  public ResponseEntity<StateResponse> createState(@Valid @RequestBody CreateStateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(stateService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<StateResponse> updateState(
      @PathVariable Long id,
      @Valid @RequestBody UpdateStateRequest request) {
    return ResponseEntity.ok(stateService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteState(@PathVariable Long id) {
    stateService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
