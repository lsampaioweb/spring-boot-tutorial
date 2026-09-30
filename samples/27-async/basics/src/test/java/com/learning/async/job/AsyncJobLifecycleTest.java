package com.learning.async.job;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import com.learning.async.core.exception.AsyncJobNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AsyncJobLifecycleTest {

  private static final long COMPLETION_TIMEOUT_NANOS = 3_000_000_000L;

  @Autowired
  private AsyncJobService asyncJobService;

  @Test
  void submittedJobRunsAsynchronouslyAndExposesItsResult() throws InterruptedException {
    AsyncJobResponse accepted = asyncJobService.submit(new AsyncJobRequest("hello", false, 250));

    assertThat(accepted.status()).isEqualTo(JobStatus.QUEUED);
    AsyncJobResponse completed = awaitTerminalState(accepted.id());

    assertThat(completed.status()).isEqualTo(JobStatus.SUCCEEDED);
    assertThat(completed.result()).isEqualTo("HELLO");
    assertThat(completed.finishedAt()).isNotNull();
  }

  @Test
  void workerFailureBecomesObservableFailedJobState() throws InterruptedException {
    AsyncJobResponse accepted = asyncJobService.submit(new AsyncJobRequest("hello", true, 0));

    AsyncJobResponse failed = awaitTerminalState(accepted.id());

    assertThat(failed.status()).isEqualTo(JobStatus.FAILED);
    assertThat(failed.failureCode()).isEqualTo("JOB_PROCESSING_FAILED");
    assertThat(failed.result()).isNull();
  }

  private AsyncJobResponse awaitTerminalState(UUID id) throws InterruptedException {
    long deadline = System.nanoTime() + COMPLETION_TIMEOUT_NANOS;
    AsyncJobResponse response = asyncJobService.find(id);

    while (response.status() == JobStatus.QUEUED || response.status() == JobStatus.RUNNING) {
      if (System.nanoTime() >= deadline) {
        throw new AssertionError("Job did not reach a terminal state before the timeout");
      }
      Thread.sleep(10);
      response = asyncJobService.find(id);
    }

    return response;
  }
}