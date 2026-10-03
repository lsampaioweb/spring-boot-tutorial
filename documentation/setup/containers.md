# Containers (Docker / Podman) for this tutorial

Integration samples use Compose under `samples/infrastructure/`. Prefer Docker
Compose or Podman Compose — both work if the shared network and env files are set.

## Shared network

Most infra stacks join an external network named `tutorial-network`. Starting
Traefik, Vault, Redis, PostgreSQL, RabbitMQ, or the OpenTelemetry Collector
creates it when missing. You can also create it once:

```bash
docker network create tutorial-network
# Podman:
podman network exists tutorial-network || podman network create tutorial-network
```

Note: `samples/17-container` uses a **different** external network,
`spring-boot-container-network` — see that sample’s README.

## Typical start

```bash
cd samples/infrastructure/{service}
cp .env.example .env   # when the service ships an example
docker compose up -d
```

Stop:

```bash
docker compose down
```

If you use Podman, replace `docker compose` with `podman compose`.

## Troubleshooting

### `docker-credential-secretservice` missing

Install Docker credential helpers, or remove the `credsStore` setting from
`~/.docker/config.json`.

### Podman: insufficient UIDs/GIDs in user namespace

Your user is missing rootless mappings in `/etc/subuid` and `/etc/subgid`.

Preflight:

```bash
whoami
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```

If either grep returns no line, ask an administrator to provision ranges, then:

```bash
usermod --add-subuids 100000-165535 --add-subgids 100000-165535 <username>
podman system migrate
```

### Traefik on rootless Podman

Port 80 may require extra configuration; see
[`samples/infrastructure/traefik/README.md`](../../samples/infrastructure/traefik/README.md).

## Next

Return to the [root README](../../README.md) Infrastructure section, or open an
integration topic (PostgreSQL, Redis, RabbitMQ, Vault, Traefik, Tracing).

[Go Back](../../README.md)

#
### Created by:

1. Luciano Sampaio.
