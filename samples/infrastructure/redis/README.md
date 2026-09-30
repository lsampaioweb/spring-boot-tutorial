# Redis

In-memory data store for this tutorial (datastore, cache, and pub/sub samples). Host port `6379`. No password; Spring samples use `REDIS_HOST=localhost` and `REDIS_PORT=6379`.

AOF is on, plus RDB snapshots every 60 seconds if at least one key changed. Data is bind-mounted at `./volumes` (`/data` in the container) and survives `compose down`.

Compose creates the shared network `tutorial-network` on first `up`. You do not need a manual `docker network create` / `podman network create` before starting Redis.

Traefik HTTP routing does not apply here (Redis is not HTTP). JVM samples talk to `localhost:6379`. Other containers on `tutorial-network` can use hostname `tutorial-redis`.

All commands below assume the tutorial repo root is your current directory, then `cd samples/infrastructure/redis`. Replace `docker` with `podman` if that is what you use.

## Prerequisites

1. Docker Compose, or Podman with Podman Compose.
1. Host port `6379` free.

Rootless Podman, once per machine:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## Start

```bash
cd samples/infrastructure/redis
docker compose up -d
```

## Verify Redis (no Spring app required)

```bash
docker compose ps
docker exec tutorial-redis redis-cli ping
```

`ps` should show healthy. `ping` should print `PONG`.

## Spring samples

| Sub-project | Path |
|-------------|------|
| Datastore | `samples/23-redis/datastore` |
| Cache layer | `samples/23-redis/cache-layer` |
| Pub/Sub | `samples/23-redis/pubsub-events` |

No extra `.env` is required for Redis. How the apps use it is in [redis.md](../../../../documentation/spring/integrations/redis.md).

Inspect data the datastore sample writes:

```bash
docker exec tutorial-redis redis-cli HGETALL products
```

## Stop

```bash
cd samples/infrastructure/redis
docker compose down
```

Data stays in `./volumes`. Delete that directory if you want a blank Redis.

`compose down` leaves `tutorial-network` in place if another stack is still using it.

## Troubleshooting

**Port 6379 already in use.**

```bash
ss -tlnp | grep ':6379 '
```

**`NOAUTH` or connection refused from the sample.** This compose file has no `requirepass`. Do not set `spring.data.redis.password` unless you add AUTH yourself.

**Permission denied on `/data`.** The container user must write `./volumes`:

```bash
sudo chown -R 999:1000 volumes
```

Rootless Podman often works without this because UIDs are mapped to your user.

**`docker-credential-secretservice` missing.** Install Docker credential helpers, or remove `credsStore` from `~/.docker/config.json`.

**Podman: `potentially insufficient UIDs or GIDs available in user namespace`.** Ask an administrator to add subuid/subgid ranges, then `podman system migrate`.

```bash
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```
