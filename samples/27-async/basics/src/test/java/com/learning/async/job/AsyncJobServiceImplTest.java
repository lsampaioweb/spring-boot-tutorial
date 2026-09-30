package com.learning.async.job;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.learning.async.core.exception.AsyncJobNotFoundException;
import com.learning.async.core.exception.AsyncJobSubmissionRejectedException;
import com.learning.async.i18n.LogMessages;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskRejectedException;

@ExtendWith(MockitoExtension.class)
class AsyncJobServiceImplTest {

  @Mock
  private AsyncJobStore jobStore;

  @Mock
  private AsyncJobWorker jobWorker;

  @Mock
  private AsyncJobMapper jobMapper;

  @Mock
  private LogMessages logMessages;

  @InjectMocks
  private AsyncJobServiceImpl asyncJobService;

  @Test
  void submitReturnsQueuedSnapshotAndRecordsSuccessfulCompletion() {
    AsyncJob job = AsyncJob.queued("hello", 0);
    AsyncJobResponse queuedResponse = response(job, JobStatus.QUEUED, null, null);
    when(jobStore.create("hello", 0)).thenReturn(job);
    when(jobWorker.process(job.id(), "hello", 0, false))
        .thenReturn(CompletableFuture.completedFuture("HELLO"));
    when(jobMapper.toResponse(job)).thenReturn(queuedResponse);

    AsyncJobResponse response = asyncJobService.submit(new AsyncJobRequest("hello", false, 0));

    assertThat(response.status()).isEqualTo(JobStatus.QUEUED);
    verify(jobStore).markSucceeded(job.id(), "HELLO");
  }

  @Test
  void submitRecordsFailedCompletionWithoutLeakingWorkerException() {
    AsyncJob job = AsyncJob.queued("hello", 0);
    when(jobStore.create("hello", 0)).thenReturn(job);
    when(jobWorker.process(job.id(), "hello", 0, true))
        .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("worker failed")));
    when(logMessages.get("log.async-job.failed", job.id())).thenReturn("Async job failed");
    when(jobMapper.toResponse(job)).thenReturn(response(job, JobStatus.QUEUED, null, null));

    asyncJobService.submit(new AsyncJobRequest("hello", true, 0));

    verify(jobStore).markFailed(job.id());
    verify(logMessages).get("log.async-job.failed", job.id());
  }

  @Test
  void submitReportsExecutorRejectionAsServiceUnavailable() {
    AsyncJob job = AsyncJob.queued("hello", 0);
    when(jobStore.create("hello", 0)).thenReturn(job);
    when(jobWorker.process(job.id(), "hello", 0, false))
        .thenThrow(new TaskRejectedException("executor unavailable"));

    assertThatThrownBy(() -> asyncJobService.submit(new AsyncJobRequest("hello", false, 0)))
        .isInstanceOf(AsyncJobSubmissionRejectedException.class);
    verify(jobStore).markFailed(job.id());
  }

  @Test
  void findThrowsWhenJobDoesNotExist() {
    UUID id = UUID.randomUUID();
    when(jobStore.find(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> asyncJobService.find(id))
        .isInstanceOf(AsyncJobNotFoundException.class);
  }

  private AsyncJobResponse response(
      AsyncJob job,
      JobStatus status,
      String result,
      String failureCode) {
    return new AsyncJobResponse(
        job.id(),
        status,
        result,
        failureCode,
        job.submittedAt(),
        job.startedAt(),
        job.finishedAt());
  }
}