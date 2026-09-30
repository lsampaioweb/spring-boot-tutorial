package com.learning.async.job;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

@Component
class AsyncJobStore {

  private final ConcurrentMap<UUID, AsyncJob> jobs = new ConcurrentHashMap<>();

  AsyncJob create(String input, long delayMs) {
    AsyncJob job = AsyncJob.queued(input, delayMs);
    jobs.put(job.id(), job);

    return job;
  }

  Optional<AsyncJob> find(UUID id) {
    return Optional.ofNullable(jobs.get(id));
  }

  void markRunning(UUID id) {
    jobs.computeIfPresent(id, (jobId, job) -> job.running());
  }

  void markSucceeded(UUID id, String result) {
    jobs.computeIfPresent(id, (jobId, job) -> job.succeeded(result));
  }

  void markFailed(UUID id) {
    jobs.computeIfPresent(id, (jobId, job) -> job.failed());
  }
}