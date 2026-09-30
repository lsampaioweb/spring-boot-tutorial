# Spring Boot + Traefik

Working sample: `samples/26-traefik`. Infrastructure: `samples/infrastructure/traefik`.

The compose runbook (start, verify, HTTPS comments, Podman port 80, stop) lives next to the files: [`samples/infrastructure/traefik/README.md`](../../../samples/infrastructure/traefik/README.md). This page is the Spring Boot side: labels, hostname, and how to run the sample behind Traefik.

HTTP on port `80` is the default. HTTPS on port `443` is commented in `docker-compose.yml`. Uncomment those `# HTTPS:` blocks after you drop `certs/cert.pem` and `certs/key.pem`.

The dashboard listens on host port `8081` so it does not collide with Spring Boot samples on `8080`.

Compose creates the shared network `tutorial-network` on first Traefik `up`. You do not need a manual `docker network create` / `podman network create` before starting Traefik.

Commands below start from the **tutorial repo root**.

## Prerequisites
1. Docker Compose, or Podman with Podman Compose
1. Traefik running (see the infrastructure README)
1. DNS or `/etc/hosts` for `app.lan.home`, unless you send a `Host` header with `curl`

`/etc/hosts` example when you have no LAN DNS:

```bash
127.0.0.1 app.lan.home
```

Before first use with rootless Podman:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## 1. Start Traefik

Follow [`samples/infrastructure/traefik/README.md`](../../../samples/infrastructure/traefik/README.md). Short version:

```bash
cd samples/infrastructure/traefik
cp .env.example .env
# Docker: comment out CONTAINER_SOCKET in .env
# Podman: keep CONTAINER_SOCKET as in .env.example
docker compose up -d
curl -fsS http://127.0.0.1:8081/ping
```

Expect `OK`. Dashboard: `http://localhost:8081/dashboard/`

## 2. Enable HTTPS (optional)

Same steps as the infrastructure README: PEM files in `certs/`, uncomment every `# HTTPS:` line, `docker compose up -d`, then switch the app labels from `web` to `websecure` + `tls=true`. HTTP then redirects to HTTPS.

## 3. Route a Spring Boot app through Traefik

Start Traefik first so `tutorial-network` exists. The app container must:

1. Join network `tutorial-network` (`external: true`)
1. Include labels like:

```yaml
labels:
  - "traefik.enable=true"
  - "traefik.http.routers.myapp.rule=Host(`app.lan.home`)"
  - "traefik.http.routers.myapp.entrypoints=web"
  - "traefik.http.services.myapp.loadbalancer.server.port=8080"
  # HTTPS: uncomment these two and comment the web entrypoint above.
  # - "traefik.http.routers.myapp.entrypoints=websecure"
  # - "traefik.http.routers.myapp.tls=true"
```

`TRAEFIK_DOMAIN` in Traefik's `.env` does not change these labels by itself. If your suffix is not `lan.home`, edit `Host(\`app.lan.home\`)` in the app compose file.

Traefik talks **HTTP** to the container port in `loadbalancer.server.port`. Do not point that port at an HTTPS-only listener.

## 4. Run `samples/26-traefik`

Production profile (the sample default) listens on **9443 inside the container**. Compose does not publish that port on the host; Traefik is the only ingress. Labels already use `Host(\`app.lan.home\`)` and `loadbalancer.server.port=9443`.

```bash
cd samples/26-traefik
mvn -q package
docker build --tag=lsampaioweb/app:1.0 .
export SECURITY_ACTUATOR_USERNAME=actuator
export SECURITY_ACTUATOR_PASSWORD=change-me
docker compose up -d
```

Podman: `podman build` and `podman compose`. The actuator username and password are required; compose fails at start if they are missing.

If the image user cannot write `./logs`, fix ownership (UID `1112` matches the tutorial Spring Boot image):

```bash
sudo chown -R 1112:1112 ./logs
```

## 5. Validate routing

If DNS (or `/etc/hosts`) resolves `app.lan.home` to this host:

```bash
curl http://app.lan.home/api/v1/users/hello
```

Without DNS:

```bash
curl -H "Host: app.lan.home" http://localhost/api/v1/users/hello
```

After HTTPS is enabled:

```bash
curl -k https://app.lan.home/api/v1/users/hello
```

Use `-k` only for a self-signed cert.

## 6. Stop

App first, then Traefik (Traefik's `down` keeps `tutorial-network` if the app is still attached):

```bash
cd samples/26-traefik && docker compose down
cd samples/infrastructure/traefik && docker compose down
```

### Troubleshooting

Infrastructure failures (port 80 on rootless Podman, Docker vs Podman socket, dashboard, `compose down` and the shared network) are in [`samples/infrastructure/traefik/README.md`](../../../samples/infrastructure/traefik/README.md).

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
