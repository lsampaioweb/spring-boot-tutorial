package com.learning.tracing.caller.demo;

import java.time.Instant;

/**
 * Aggregated demo response combining caller and callee identities.
 */
public record TraceDemoResponse(String caller, String callee, Instant timestamp) {
}
