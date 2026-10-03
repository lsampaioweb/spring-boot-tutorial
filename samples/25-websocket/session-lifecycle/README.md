# 25 — WebSocket session lifecycle

Teaches session connect/disconnect detection, presence broadcasts, admin force-disconnect, and abuse kicks.

## Layout

| Path | Role | Port |
| --- | --- | --- |
| `server/` | STOMP `/ws`, presence `/topic/presence`, admin REST | `8092` |
| `client/` | Browser UI for presence, kick, and burst abuse | `8093` |

## What you will see

1. **Connect detection** — `SessionConnectEvent` registers the session and publishes `CONNECTED`.
1. **Disconnect detection** — `SessionDisconnectEvent` removes the session and publishes `DISCONNECTED`.
1. **Force disconnect** — `DELETE /api/v1/sessions/{id}` closes the raw WebSocket session (`KICKED`).
1. **Abuse disconnect** — more than 5 messages in 3 seconds closes with `POLICY_VIOLATION` (`RATE_LIMITED`).
1. **Heartbeats** — simple broker heartbeats every 10s help detect dead clients.

## Configuration

| Variable | Purpose |
| --- | --- |
| `WEBSOCKET_ADMIN_PASSWORD` | HTTP Basic password for session admin REST |
| `WEBSOCKET_ADMIN_USERNAME` | Optional (default `session-admin`) |

## Run

```bash
cd samples/25-websocket/session-lifecycle/server
export WEBSOCKET_ADMIN_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

```bash
cd samples/25-websocket/session-lifecycle/client
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Open `http://localhost:8093/`.

1. Connect with a display name.
1. Watch presence events.
1. Refresh sessions (admin Basic auth) and **Kick** a session.
1. Click **Send burst** to trigger an abuse disconnect.

Swagger UI (development): `http://localhost:8092/swagger-ui/index.html`

## Tests

```bash
cd samples/25-websocket/session-lifecycle/server && mvn test
cd samples/25-websocket/session-lifecycle/client && mvn test
```
