package com.learning.async.job;

import org.springframework.stereotype.Component;

@Component
class AsyncJobMapper {

  AsyncJobResponse toResponse(AsyncJob job) {
    return new AsyncJobResponse(
        job.id(),
        job.status(),
        job.result(),
        job.failureCode(),
        job.submittedAt(),
        job.startedAt(),
        job.finishedAt());
  }
}