# 23 — Redis

Three independent modules. Start Redis (and Postgres for cache-layer) first.

| Module | Role | Infra |
| --- | --- | --- |
| [datastore](datastore/README.md) | Redis as primary store | Redis |
| [cache-layer](cache-layer/README.md) | Redis cache over Postgres | Redis + Postgres |
| [pubsub-events](pubsub-events/README.md) | Pub/Sub events | Redis |

Infra: [samples/infrastructure/redis/README.md](../infrastructure/redis/README.md),
[samples/infrastructure/postgres/README.md](../infrastructure/postgres/README.md)

Topic: [documentation/spring/integrations/redis.md](../../documentation/spring/integrations/redis.md)
