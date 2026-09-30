package com.learning.async.job;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/** Executes the sample job on Spring's asynchronous task executor. */
@Component
public class AsyncJobWorker {

  private final AsyncJobStore jobStore;

  AsyncJobWorker(AsyncJobStore jobStore) {
    this.jobStore = jobStore;
  }

  /** Completes the deterministic sample operation asynchronously. */
  @Async
  public CompletableFuture<String> process(UUID jobId, String input, long delayMs, boolean failForDemo) {
    jobStore.markRunning(jobId);

    try {
      Thread.sleep(delayMs);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(ex);
    }

    if (failForDemo) {
      throw new IllegalStateException("Requested demonstration failure");
    }

    return CompletableFuture.completedFuture(input.toUpperCase(Locale.ROOT));
  }
}