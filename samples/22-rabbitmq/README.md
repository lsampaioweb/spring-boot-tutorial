# 22 — RabbitMQ

Four independent modules, one exchange type each. Start RabbitMQ first.

## Infrastructure

```bash
cd samples/infrastructure/rabbitmq
cp .env.example .env   # if present
docker compose up -d
```

Runbook: [samples/infrastructure/rabbitmq/README.md](../infrastructure/rabbitmq/README.md)

## Modules

| Module | Endpoint |
| --- | --- |
| [direct](direct/README.md) | `POST /api/v1/messages/direct` |
| [fanout](fanout/README.md) | `POST /api/v1/messages/fanout` |
| [topic](topic/README.md) | `POST /api/v1/messages/topic` |
| [headers](headers/README.md) | `POST /api/v1/messages/headers` |

Shared env (required passwords have no defaults):

```bash
export RABBITMQ_DEFAULT_USER=...
export RABBITMQ_DEFAULT_PASS=...
```

Topic: [documentation/spring/integrations/rabbitmq.md](../../documentation/spring/integrations/rabbitmq.md)
