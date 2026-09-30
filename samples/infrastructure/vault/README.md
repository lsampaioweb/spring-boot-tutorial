# Vault

HashiCorp Vault for this tutorial. File storage (secrets survive `compose down`). HTTP on host port `8200`. HTTPS is Traefik termination: uncomment the `labels:` block in `docker-compose.yml` when Traefik is running.

This is **not** `vault server -dev`. After every start the process is **sealed** until you unseal it. Compose healthcheck uses `vault status`, so the container looks unhealthy until you unseal.

Compose creates the shared network `tutorial-network` on first `up`. You do not need a manual `docker network create` / `podman network create` before starting Vault.

All commands below assume the tutorial repo root is your current directory, then `cd samples/infrastructure/vault`. Replace `docker` with `podman` if that is what you use.

## Prerequisites

1. Docker Compose, or Podman with Podman Compose.
1. Host port `8200` free.

Rootless Podman, once per machine:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## Start

```bash
cd samples/infrastructure/vault
cp .env.example .env
docker compose up -d
```

Wait until the container is running (`docker compose ps`), then continue. Init fails if Vault is not up yet.

Save the Unseal Key(s) and Initial Root Token somewhere private. You need them after every restart. They are not stored in this repo.

### First start only (initialize)

```bash
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 tutorial-vault \
  vault operator init -key-shares=1 -key-threshold=1
```

Copy the `Unseal Key 1` and `Initial Root Token` from that output.

### Every start (unseal)

```bash
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 tutorial-vault \
  vault operator unseal
```

Paste the unseal key when prompted, or pass it as the last argument.

### First start only (KV engine + sample secrets)

```bash
export VAULT_TOKEN="<Initial Root Token>"

docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN tutorial-vault \
  vault secrets enable -path=secret kv-v2

docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN tutorial-vault \
  vault kv put secret/spring-boot-tutorial \
    api-secret=my-api-secret \
    db-password=my-db-password
```

`-e VAULT_TOKEN` with no value passes your shell's `VAULT_TOKEN` into the container.

Confirm the secret:

```bash
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN tutorial-vault \
  vault kv get secret/spring-boot-tutorial
```

## Verify Vault (no Spring app required)

```bash
docker compose ps
curl -fsS http://127.0.0.1:8200/v1/sys/health
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 tutorial-vault vault status
```

After unseal, `ps` should show healthy and `curl` should return JSON (HTTP 200). Before unseal, health is 501 (uninitialized) or 503 (sealed).

UI: `http://localhost:8200/ui/` — log in with the Initial Root Token.

## Spring samples

Put the Initial Root Token in each sub-project `.env` as `VAULT_TOKEN`. `VAULT_URI` stays `http://localhost:8200`.

How the apps load secrets is in [vault.md](../../../../documentation/spring/integrations/vault.md).

## Traefik hostname (optional)

Uncomment the Traefik `labels:` block in `docker-compose.yml` (and the HTTPS pair if Traefik HTTPS is on). Hostname: `vault.lan.home`.

If this machine is not already `vault.lan.home` in DNS, add this line to `/etc/hosts` (requires sudo):

```text
127.0.0.1 vault.lan.home
```

Start Traefik first so `tutorial-network` already exists, or start Vault first; either compose file can create the network.

## Stop

```bash
cd samples/infrastructure/vault
docker compose down
```

Data stays in `volumes/file`. The next `up` still needs the unseal command. Init, `secrets enable`, and `kv put` are first-start only.

`compose down` leaves `tutorial-network` in place if another stack is still using it.

## Troubleshooting

**Lost unseal key or root token.** You cannot unseal. Delete `volumes/file` (this wipes secrets), `compose up -d`, and run the first-start commands again. Update `VAULT_TOKEN` in the Spring `.env` files.

**`ps` shows unhealthy right after `up`.** Expected until you unseal.

**Permission denied on `/vault/file` or `/vault/logs`.** The container user must write those bind mounts:

```bash
sudo chown -R 100:1000 volumes/file volumes/logs
```

Rootless Podman often works without this because UIDs are mapped to your user.

**Port 8200 already in use.**

```bash
ss -tlnp | grep ':8200 '
```

**`docker-credential-secretservice` missing.** Install Docker credential helpers, or remove `credsStore` from `~/.docker/config.json`.

**Podman: `potentially insufficient UIDs or GIDs available in user namespace`.** Ask an administrator to add subuid/subgid ranges, then `podman system migrate`.

```bash
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```
