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
cp .env.example .env
```

```bash
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

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
