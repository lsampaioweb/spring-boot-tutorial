# Traefik

Reverse proxy for this tutorial. HTTP on port `80` is the default. HTTPS on `443` is optional (commented in `docker-compose.yml`).

The dashboard uses host port `8081` so it does not collide with Spring Boot samples on `8080`.

Compose creates the shared network `tutorial-network` on first `up`. You do not need `docker network create` / `podman network create` before starting Traefik.

All commands below assume the tutorial repo root is your current directory, then `cd samples/infrastructure/traefik`.

## Prerequisites

1. Docker Compose, or Podman with Podman Compose.
1. Host ports `80` and `8081` free (`443` too if you enable HTTPS).
1. Optional: DNS or `/etc/hosts` for `app.lan.home` if you will open that hostname in a browser.

Rootless Podman, once per machine:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## Start

```bash
cd samples/infrastructure/traefik
cp .env.example .env
```

Edit `.env`:

1. **Docker:** comment out `CONTAINER_SOCKET`. Compose then uses `/var/run/docker.sock`. If you leave the Podman path in place, Traefik starts but cannot see other containers.
1. **Podman rootless:** keep `CONTAINER_SOCKET=${XDG_RUNTIME_DIR}/podman/podman.sock`.

```bash
docker compose up -d
# Podman: podman compose up -d
```

## Verify Traefik (no Spring app required)

```bash
docker compose ps
curl -fsS http://127.0.0.1:8081/ping
```

`ps` should show healthy. `curl` should print `OK`.

Dashboard (insecure API on purpose for this tutorial; reachable on the LAN):

1. `http://localhost:8081/dashboard/`

## Route a hostname

Sample apps use `Host(\`app.lan.home\`)`. `TRAEFIK_DOMAIN` in `.env` is a reminder only. If you change the suffix, also edit the labels in `samples/26-traefik/docker-compose.yml`.

If this machine is not already `app.lan.home` in DNS:

```bash
# Linux: add a line to /etc/hosts (requires sudo)
127.0.0.1 app.lan.home
```

How to put a Spring Boot container behind Traefik, including `samples/26-traefik`, is in [traefik.md](../../../../documentation/spring/integrations/traefik.md).

## Enable HTTPS (optional)

HTTP keeps working until you uncomment the HTTPS blocks. After you do, Traefik redirects HTTP to HTTPS.

1. Put PEM files in this folder as `certs/cert.pem` and `certs/key.pem` (homelab CA, mkcert, or a self-signed cert). Do not commit them; `certs/` ignores `*`.
1. In `docker-compose.yml`, uncomment every line marked `# HTTPS:`:
   1. Port `443:443`
   1. `websecure` entrypoint, HTTP→HTTPS redirect, and file provider flags
   1. Volume mounts for `./certs` and `./dynamic`
1. Recreate:

```bash
docker compose up -d
```

1. On the app compose file, comment `entrypoints=web` and uncomment `entrypoints=websecure` plus `tls=true`.

Self-signed cert for local tests only:

```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout certs/key.pem -out certs/cert.pem \
  -subj "/CN=app.lan.home" \
  -addext "subjectAltName=DNS:app.lan.home,DNS:*.lan.home"
```

Browsers will warn on a self-signed cert. A homelab CA signed for `*.lan.home` will not.

## Stop

```bash
cd samples/infrastructure/traefik
docker compose down
```

`compose down` leaves `tutorial-network` in place if another stack is still using it.

## Troubleshooting

**`CONTAINER_SOCKET` points at a missing Podman socket (Docker users).** Comment it out in `.env` and run `docker compose up -d` again.

**Rootless Podman cannot bind port 80.** Typical error: `rootlessport cannot expose privileged port 80`. Lower the unprivileged port start (as root), then retry:

```bash
echo 'net.ipv4.ip_unprivileged_port_start=80' | sudo tee /etc/sysctl.d/99-unprivileged-ports.conf
sudo sysctl --system
```

**Port already in use.**

```bash
ss -tlnp | grep -E ':80 |:443 |:8081 '
```

Stop the other process, or change the host port on the left side of the mapping (`8081:8080` is Traefik's dashboard).

**Dashboard opens, but no Spring app appears.** Traefik only picks up containers that (1) share `tutorial-network`, (2) have `traefik.enable=true`, and (3) are visible through the mounted engine socket. Start Traefik first, then the app compose file.

**`docker-credential-secretservice` missing.** Install Docker credential helpers, or remove `credsStore` from `~/.docker/config.json`.

**Podman: `potentially insufficient UIDs or GIDs available in user namespace`.** Ask an administrator to add subuid/subgid ranges, then `podman system migrate`.

```bash
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```
