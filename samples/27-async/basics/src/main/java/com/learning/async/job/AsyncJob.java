package com.learning.async.job;

import java.time.Instant;
import java.util.UUID;

record AsyncJob(
    UUID id,
    String input,
    long delayMs,
    JobStatus status,
    String result,
    String failureCode,
    Instant submittedAt,
    Instant startedAt,
    Instant finishedAt) {

  static AsyncJob queued(String input, long delayMs) {
    return new AsyncJob(
        UUID.randomUUID(), input, delayMs, JobStatus.QUEUED, null, null,
        Instant.now(), null, null);
  }

  AsyncJob running() {
    return new AsyncJob(
        id, input, delayMs, JobStatus.RUNNING, null, null,
        submittedAt, Instant.now(), null);
  }

  AsyncJob succeeded(String result) {
    return new AsyncJob(
        id, input, delayMs, JobStatus.SUCCEEDED, result, null,
        submittedAt, startedAt, Instant.now());
  }

  AsyncJob failed() {
    return new AsyncJob(
        id, input, delayMs, JobStatus.FAILED, null, "JOB_PROCESSING_FAILED",
        submittedAt, startedAt, Instant.now());
  }
}