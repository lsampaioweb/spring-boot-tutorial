package com.learning.redis.pubsub;

import java.time.Instant;

record ProductPublishedEvent(Long productId, String productName, Instant occurredAt) {
}