package com.learning.async.job;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Tag(name = "Asynchronous jobs")
class AsyncJobRestController {

  private final AsyncJobService asyncJobService;

  /** Accepts a job for background processing. */
  @PostMapping
  @Operation(summary = "{openapi.jobs.submit.summary}")
  public ResponseEntity<AsyncJobResponse> submit(
      @Valid @RequestBody AsyncJobRequest request,
      UriComponentsBuilder uriBuilder) {
    AsyncJobResponse response = asyncJobService.submit(request);
    URI location = uriBuilder.path("/api/v1/jobs/{id}").buildAndExpand(response.id()).toUri();

    return ResponseEntity.accepted().location(location).body(response);
  }

  /** Returns the latest state of a submitted job. */
  @GetMapping("/{id}")
  @Operation(summary = "{openapi.jobs.find.summary}")
  public ResponseEntity<AsyncJobResponse> find(@PathVariable UUID id) {
    return ResponseEntity.ok(asyncJobService.find(id));
  }
}