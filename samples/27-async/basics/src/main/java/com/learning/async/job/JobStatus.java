package com.learning.async.job;

/** Describes the lifecycle state of an asynchronous job. */
public enum JobStatus {
  QUEUED,
  RUNNING,
  SUCCEEDED,
  FAILED
}