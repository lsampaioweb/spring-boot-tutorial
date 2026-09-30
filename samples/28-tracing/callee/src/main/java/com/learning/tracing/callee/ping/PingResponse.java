package com.learning.tracing.callee.ping;

import java.time.Instant;

/**
 * Payload returned by the callee ping endpoint.
 */
public record PingResponse(String service, Instant timestamp) {
}
