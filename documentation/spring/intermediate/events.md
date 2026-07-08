## Spring Application Events

This guide shows how to use Spring Application Events to decouple cross-package communication and avoid circular service dependencies.

### Why use internal events?

Use events when one feature must react to another feature without direct service injection.

Example:

1. Chat endpoint receives a message.
1. Endpoint publishes `ChatMessagePublishedEvent` through `ApplicationEventPublisher`.
1. Async listener handles audit logging independently.

This keeps endpoint logic focused and removes direct coupling to audit infrastructure.

### Where to see a working sample

Path: `samples/20-websocket/server`

Relevant classes:

1. `chat/ChatMessageEventPublisher.java`
1. `chat/ChatMessagePublishedEvent.java`
1. `chat/ChatMessageAuditListener.java`

### Event payload rules

1. Use immutable records for event payloads.
1. Extract thread-local values before publish (for example `LocaleContextHolder`).
1. Pass extracted values inside the event payload.

### @Async listener rules

1. Add `@EnableAsync` in application configuration when async listeners exist.
1. Use `@Async` only for blocking I/O listeners where latency is significant.
1. Keep simple in-memory listeners synchronous for ordering and immediate visibility.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
