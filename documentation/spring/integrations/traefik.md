# Spring Boot + Traefik

This page explains the Traefik infrastructure project and how to route Spring Boot containers through it.

## Prerequisites
1. Podman with Podman Compose
1. A Spring Boot app running in a container with Traefik labels

Before first use with rootless Podman:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## 1. Start Traefik Infrastructure

For Podman, map its socket by setting `CONTAINER_SOCKET` first:

```bash
podman network exists tutorial-network || podman network create tutorial-network
cp .env.example .env
cd samples/infrastructure/traefik
podman compose up -d
```

Check status:

```bash
podman compose ps
```

Traefik dashboard (tutorial mode):
1. URL: `http://localhost:8080`

## 2. Route a Spring Boot App Through Traefik

Your app container must:
1. Join network `tutorial-network`
1. Include labels like:

```yaml
labels:
  - "traefik.enable=true"
  - "traefik.http.routers.myapp.rule=Host(`app.localhost`)"
  - "traefik.http.routers.myapp.entrypoints=web"
  - "traefik.http.services.myapp.loadbalancer.server.port=8080"
```

The sample in `samples/17-traefik` already includes labels and uses the shared external network `tutorial-network`.

## 3. Validate Routing

If your app is exposed as `app.localhost`:

```bash
curl -H "Host: app.localhost" http://localhost/api/v1/hello
```

## 4. Stop Traefik Infrastructure

```bash
cd samples/infrastructure/traefik
podman compose down
```

### Troubleshooting

If startup fails with `docker-credential-secretservice` missing while using Docker Compose, install Docker credential helpers or remove `credsStore` from `~/.docker/config.json`.

If `podman compose up` fails with `potentially insufficient UIDs or GIDs available in user namespace`, your rootless Podman user is missing subuid/subgid mappings. Ask an administrator to add ranges for your user in `/etc/subuid` and `/etc/subgid`, then run:

```bash
podman system migrate
```

Preflight check:

```bash
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```

If either command returns no line, ask an administrator to add unique ranges, for example:

```bash
usermod --add-subuids 100000-165535 --add-subgids 100000-165535 <username>
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
