package com.learning.async.core.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

/** Reports that the requested in-memory job does not exist. */
public class AsyncJobNotFoundException extends AppException {

  /** Creates a not-found error for the specified job identifier. */
  public AsyncJobNotFoundException(UUID id) {
    super("error.async-job.not-found", "ASYNC_JOB_NOT_FOUND", new Object[] { id }, HttpStatus.NOT_FOUND);
  }
}