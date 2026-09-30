## Spring Application Events

This guide shows how to use Spring Application Events to decouple cross-package communication and avoid circular service dependencies.

### Why use internal events?

Use events when one feature must react to another feature without direct service injection.

Example:

1. Message endpoint receives a payload.
1. Service publishes `MessagePublishedEvent` through `ApplicationEventPublisher`.
1. Async listener handles audit logging independently.

This keeps endpoint logic focused and removes direct coupling to audit infrastructure.

### Where to see a working sample

Path: `samples/15-events`

Relevant classes:

1. `message/MessageEventPublisher.java`
1. `message/MessagePublishedEvent.java`
1. `message/MessageAuditListener.java`

### Event payload rules

1. Use immutable records for event payloads.
1. Extract thread-local values before publish (for example `LocaleContextHolder`).
1. Pass extracted values inside the event payload.

### @Async listener rules

1. Add `@EnableAsync` when async listeners exist (see also [async.md](async.md) and `samples/27-async/basics`).
1. Use `@Async` only for blocking I/O listeners where latency is significant.
1. Keep simple in-memory listeners synchronous for ordering and immediate visibility.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
