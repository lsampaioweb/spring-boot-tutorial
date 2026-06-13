## WebSocket

This tutorial demonstrates the two sides of a WebSocket integration:

1. A **WebSocket server** using Spring Boot STOMP support.
1. A **WebSocket client** with a small UI that keeps a persistent open connection.

The key objective is to understand the open socket lifecycle (connect, subscribe, send, receive, disconnect), not only to render a page.

### Why split this tutorial into server and client projects?

1. It makes responsibilities explicit.

	- The **server** focuses on endpoint registration, routing, and broadcasting.
	- The **client** focuses on connection management and message flow.

1. It mirrors real systems.

	In production, WebSocket servers and front-end clients are often deployed separately.

1. It simplifies debugging.

	You can run each side independently and validate where the issue is: handshake, subscription, or message routing.

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

### Server project

Path: `samples/20-websocket/server`

Main decisions:

1. Use `spring-boot-starter-websocket` with STOMP.
1. Register endpoint `/ws` with SockJS fallback.
1. Use `/app` as application destination prefix.
1. Broadcast on `/topic/messages`.

Core flow:

1. Client sends to `/app/chat.send`.
1. Server handles the payload and forwards to `/topic/messages`.
1. All subscribed clients receive the message.

The sample also tracks active session count and exposes:

- `GET /api/v1/websocket/connections`

This helps visualize open connection behavior.

### Client project

Path: `samples/20-websocket/client`

The client project serves one UI page that:

1. Connects to the server WebSocket endpoint.
1. Subscribes to `/topic/messages`.
1. Sends messages to `/app/chat.send`.
1. Shows connect/disconnect state.

Default server URL in UI:

- `http://localhost:8090/ws`

### Run the tutorial

1. Start the server:

	```bash
	cd samples/20-websocket/server
	mvn spring-boot:run
	```

1. Start the client:

	```bash
	cd samples/20-websocket/client
	mvn spring-boot:run
	```

1. Open the client UI:

	- `http://localhost:8091/`

1. Connect and exchange messages.

1. Check open sessions on server:

	- `http://localhost:8090/api/v1/websocket/connections`

### Why STOMP and SockJS in this tutorial?

1. STOMP provides a simple messaging model (destinations, subscribe, send) over WebSocket.
1. SockJS improves compatibility and gives fallback transport behavior when native WebSocket is unavailable.
1. For teaching, this is clearer than implementing raw low-level WebSocket frames.

### Client-side observability

The client script writes a small set of browser logs to help you debug the connection lifecycle without flooding the console.

1. `console.debug` for message send/receive flow.
1. `console.info` for connect/disconnect milestones.
1. `console.warn` for connection failures or invalid user input.

Keep this principle for tutorials: log enough to understand runtime behavior, but avoid noisy frame-level output.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
