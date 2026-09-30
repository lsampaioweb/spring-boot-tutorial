package com.learning.tracing.callee.ping;

/**
 * Builds the ping response for inbound traced requests.
 */
public interface PingService {

  /**
   * Returns a ping payload that identifies this service.
   *
   * @return ping response
   */
  PingResponse ping();
}
