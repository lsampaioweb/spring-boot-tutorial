package com.learning.events.message;

import java.time.Instant;

record MessagePublishedEvent(String sender, int contentLength, Instant publishedAt) {
}
