package com.learning.async.core.exception;

import org.springframework.http.HttpStatus;

/** Reports that the task executor could not accept a submitted job. */
public class AsyncJobSubmissionRejectedException extends AppException {

  /** Creates a service-unavailable error for a rejected job submission. */
  public AsyncJobSubmissionRejectedException(Throwable cause) {
    super(
        "error.async-job.submission.rejected",
        "ASYNC_JOB_SUBMISSION_REJECTED",
        new Object[0],
        HttpStatus.SERVICE_UNAVAILABLE,
        cause);
  }
}