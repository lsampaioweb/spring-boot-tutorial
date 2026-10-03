# RabbitMQ

Message broker for this tutorial (`samples/22-rabbitmq`: direct, fanout, topic, headers). AMQP on host port `5672`. Management UI on `15672`.

Credentials come only from `.env` (copy `.env.example` and change them). Compose fails if `RABBITMQ_DEFAULT_USER` or `RABBITMQ_DEFAULT_PASS` are unset. The Spring samples read the same variable names — they have no fallback passwords in YAML.

`RABBITMQ_DEFAULT_*` is applied only on first init. Changing `.env` later does not update an existing volume; delete `./volumes` to start over.

A stable `hostname` is set so the Erlang node name matches the bind-mounted data.

Compose creates the shared network `tutorial-network` on first `up`. You do not need a manual `docker network create` / `podman network create` before starting RabbitMQ.

JVM samples use `localhost:5672`. Other containers on `tutorial-network` can use hostname `tutorial-rabbitmq`. The management UI can go through Traefik if you uncomment the `labels:` block (`rabbitmq.lan.home`).

All commands below assume the tutorial repo root is your current directory, then `cd samples/infrastructure/rabbitmq`. Replace `docker` with `podman` if that is what you use.

## Prerequisites

1. Docker Compose, or Podman with Podman Compose.
1. Host ports `5672` and `15672` free.

Rootless Podman, once per machine:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## Start

```bash
cd samples/infrastructure/rabbitmq
cp .env.example .env
# edit .env — do not commit it
set -a && source .env && set +a
docker compose up -d
```

Wait until healthy:

```bash
docker compose ps
docker exec tutorial-rabbitmq rabbitmq-diagnostics -q ping
```

Management UI: `http://localhost:15672` — log in with `RABBITMQ_DEFAULT_USER` / `RABBITMQ_DEFAULT_PASS` from `.env`.

Optional Traefik hostname `rabbitmq.lan.home`: uncomment the `labels:` block in `docker-compose.yml`. If this machine is not already that name in DNS, add this line to `/etc/hosts` (requires sudo):

```text
127.0.0.1 rabbitmq.lan.home
```

## Spring samples

```bash
cd samples/infrastructure/rabbitmq
set -a && source .env && set +a
```

Then run a sub-project (`direct`, `fanout`, `topic`, or `headers`). The apps read `RABBITMQ_DEFAULT_USER` and `RABBITMQ_DEFAULT_PASS`. How they use exchanges is in [rabbitmq.md](../../../documentation/spring/integrations/rabbitmq.md).

## Stop

```bash
cd samples/infrastructure/rabbitmq
docker compose down
```

Data stays in `./volumes`. Delete that directory if you want a blank broker (and a chance to change the default user).

`compose down` leaves `tutorial-network` in place if another stack is still using it.

## Troubleshooting

**`RABBITMQ_DEFAULT_PASS:?set RABBITMQ_DEFAULT_PASS in .env`.** Copy `.env.example` to `.env` in this folder. Compose interpolates that file; there is no password in `docker-compose.yml`.

**Login to 15672 fails after you edited `.env`.** Default user/password are created only on first boot. Keep the original values or delete `./volumes` and start again.

**Port 5672 or 15672 already in use.**

```bash
ss -tlnp | grep -E ':5672 |:15672 '
```

**Permission denied on `/var/lib/rabbitmq`.**

```bash
sudo chown -R 999:999 volumes
```

Rootless Podman often works without this because UIDs are mapped to your user.

**`docker-credential-secretservice` missing.** Install Docker credential helpers, or remove `credsStore` from `~/.docker/config.json`.

**Podman: `potentially insufficient UIDs or GIDs available in user namespace`.** Ask an administrator to add subuid/subgid ranges, then `podman system migrate`.

```bash
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```
