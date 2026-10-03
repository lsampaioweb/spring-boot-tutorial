## WebSocket

This tutorial demonstrates STOMP-over-WebSocket in Spring Boot.

Samples:

1. [`samples/25-websocket/basics`](../../../samples/25-websocket/basics) — chat lifecycle (connect, subscribe, send, receive, disconnect).
1. [`samples/25-websocket/session-lifecycle`](../../../samples/25-websocket/session-lifecycle) — presence, disconnect detection, admin kick, abuse force-disconnect.

### Why WebSocket and not REST polling?

REST polling opens and closes a connection repeatedly. WebSocket keeps one long-lived TCP connection, which reduces repeated HTTP overhead and enables server push.

Use WebSocket when:

1. The server must push updates as soon as they happen.
1. You need low-latency bidirectional communication.
1. Frequent polling would be wasteful.

Use REST when:

1. Data is requested occasionally.
1. You do not need real-time push.
1. Simpler infrastructure is preferred.

### Basics sample

Paths: `samples/25-websocket/basics/server` and `.../client`.

Main decisions:

1. Use `spring-boot-starter-websocket` with STOMP.
1. Register endpoint `/ws` with SockJS fallback.
1. Use `/app` as application destination prefix.
1. Broadcast on `/topic/messages`.

Core flow:

1. Client sends to `/app/chat.send`.
1. Server handles the payload, publishes an internal `ChatMessagePublishedEvent`, and forwards to `/topic/messages`.
1. All subscribed clients receive the message.

Runbook: [`samples/25-websocket/basics/README.md`](../../../samples/25-websocket/basics/README.md).

### Session lifecycle sample

Paths: `samples/25-websocket/session-lifecycle/server` and `.../client`.

This sample focuses on connection control:

1. Detect connect/disconnect with `SessionConnectEvent` / `SessionDisconnectEvent`.
1. Broadcast presence on `/topic/presence`.
1. Store the raw `WebSocketSession` so the server can force-close it.
1. Admin REST `DELETE /api/v1/sessions/{id}` kicks a client.
1. Rate-limit inbound messages and close abusive sessions with `CloseStatus.POLICY_VIOLATION`.
1. Enable STOMP heartbeats on the simple broker.

Runbook: [`samples/25-websocket/session-lifecycle/README.md`](../../../samples/25-websocket/session-lifecycle/README.md).

### Why STOMP and SockJS in this tutorial?

1. STOMP provides a simple messaging model (destinations, subscribe, send) over WebSocket.
1. SockJS improves compatibility and gives fallback transport behavior when native WebSocket is unavailable.
1. For teaching, this is clearer than implementing raw low-level WebSocket frames.

### Allowed origins

Allowed origins are never `*`. Development lists the local client origin explicitly.
Production loads `WEBSOCKET_ALLOWED_ORIGIN`. An empty or wildcard origin list fails startup.

## Previous

[Vault](../integrations/vault.md).

## Next
[Traefik](../integrations/traefik.md) — `samples/26-traefik`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
