package com.learning.tracing.caller.demo;

import java.time.Instant;

/**
 * Callee ping payload deserialized by the caller.
 */
public record PingResponse(String service, Instant timestamp) {
}
