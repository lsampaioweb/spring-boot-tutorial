package com.learning.async.job;

import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import com.learning.async.core.exception.AsyncJobNotFoundException;
import com.learning.async.core.exception.AsyncJobSubmissionRejectedException;
import com.learning.async.i18n.LogMessages;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class AsyncJobServiceImpl implements AsyncJobService {

  private static final String LOG_JOB_FAILED = "log.async-job.failed";

  private final AsyncJobStore jobStore;
  private final AsyncJobWorker jobWorker;
  private final AsyncJobMapper jobMapper;
  private final LogMessages logMessages;

  @Override
  public AsyncJobResponse submit(AsyncJobRequest request) {
    AsyncJob job = jobStore.create(request.input(), request.delayMs());

    try {
      CompletionStage<String> completion = jobWorker.process(
          job.id(), request.input(), request.delayMs(), Boolean.TRUE.equals(request.failForDemo()));
      completion.whenComplete((result, failure) -> recordOutcome(job.id(), result, failure));
    } catch (TaskRejectedException ex) {
      jobStore.markFailed(job.id());
      throw new AsyncJobSubmissionRejectedException(ex);
    }

    return jobMapper.toResponse(job);
  }

  @Override
  public AsyncJobResponse find(UUID id) {
    AsyncJob job = jobStore.find(id).orElseThrow(() -> new AsyncJobNotFoundException(id));

    return jobMapper.toResponse(job);
  }

  private void recordOutcome(UUID jobId, String result, Throwable failure) {
    if (failure == null) {
      jobStore.markSucceeded(jobId, result);
      return;
    }

    jobStore.markFailed(jobId);
    Throwable cause = failure instanceof CompletionException && failure.getCause() != null
        ? failure.getCause()
        : failure;
    log.error(logMessages.get(LOG_JOB_FAILED, jobId), cause);
  }
}