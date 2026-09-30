package com.learning.tracing.caller.demo;

/**
 * Runs the cross-JVM tracing demo against the callee.
 */
public interface TraceDemoService {

  /**
   * Calls the callee and returns a combined demo payload.
   *
   * @return demo response
   */
  TraceDemoResponse runDemo();
}
