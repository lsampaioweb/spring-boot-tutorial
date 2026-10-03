# 25 — WebSocket basics

Chat sample with a STOMP server and a Thymeleaf browser client.

## Layout

| Path | Role | Port |
| --- | --- | --- |
| `server/` | STOMP endpoint `/ws`, broadcast `/topic/messages` | `8090` |
| `client/` | Browser UI for connect / send / disconnect | `8091` |

## Run

```bash
cd samples/25-websocket/basics/server
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

```bash
cd samples/25-websocket/basics/client
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Open `http://localhost:8091/`. Connection count: `http://localhost:8090/api/v1/chat/connections`.
