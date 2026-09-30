package com.learning.async.job;

import java.time.Instant;
import java.util.UUID;

/** Reports the current state and outcome of an asynchronous job. */
public record AsyncJobResponse(
    UUID id,
    JobStatus status,
    String result,
    String failureCode,
    Instant submittedAt,
    Instant startedAt,
    Instant finishedAt) {
}