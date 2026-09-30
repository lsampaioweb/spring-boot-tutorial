# Spring Boot + HashiCorp Vault

Working samples: `samples/24-vault`. Infrastructure: `samples/infrastructure/vault`.

The compose runbook (start, init, unseal, seed, verify, stop) lives next to the files: [`samples/infrastructure/vault/README.md`](../../../samples/infrastructure/vault/README.md). This page is the Spring Boot side.

Vault uses **file storage**, not `-dev`. There is no fixed `tutorial-root-token`. After `compose up`, unseal with the key from `vault operator init`, then copy the Initial Root Token into each sample `.env` as `VAULT_TOKEN`.

On startup, `VaultSecretRegistry` loads configured secrets into an in-memory cache. `single-secret` and `multiple-secrets` do not call Vault again. `secret-rotation` reloads on demand.

| Sub-project | Path | Demonstrates |
|-------------|------|--------------|
| `single-secret` | `samples/24-vault/single-secret` | Load a single secret at startup |
| `multiple-secrets` | `samples/24-vault/multiple-secrets` | Load a list of secrets defined in `application.yml` |
| `secret-rotation` | `samples/24-vault/secret-rotation` | Reload secrets at runtime without restarting the application |

Commands below start from the **tutorial repo root**. Replace `docker` with `podman` if that is what you use.

## Prerequisites
1. Docker Compose, or Podman with Podman Compose
1. Java 25
1. Maven 3.9+

## 1. Start Vault

Follow [`samples/infrastructure/vault/README.md`](../../../samples/infrastructure/vault/README.md). Short version:

```bash
cd samples/infrastructure/vault
cp .env.example .env
docker compose up -d
```

First start only — save the Unseal Key and Initial Root Token:

```bash
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 tutorial-vault \
  vault operator init -key-shares=1 -key-threshold=1
```

Every start (including the first):

```bash
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 tutorial-vault \
  vault operator unseal
```

First start only — KV v2 + sample secrets:

```bash
export VAULT_TOKEN="<Initial Root Token>"

docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN tutorial-vault \
  vault secrets enable -path=secret kv-v2

docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN tutorial-vault \
  vault kv put secret/spring-boot-tutorial \
    api-secret=my-api-secret \
    db-password=my-db-password
```

```text
secret/spring-boot-tutorial
  api-secret=my-api-secret
  db-password=my-db-password
```

Check:

```bash
curl -fsS http://127.0.0.1:8200/v1/sys/health
```

UI: `http://localhost:8200/ui/`

Optional Traefik hostname `vault.lan.home`: uncomment the `labels:` block in the infrastructure `docker-compose.yml` (Traefik must be running). Samples still use `http://localhost:8200`.

## 2. Configure the sample

```bash
cd samples/24-vault/single-secret
cp .env.example .env
```

Set `VAULT_TOKEN` to the Initial Root Token. Repeat for `multiple-secrets` and `secret-rotation` when you run those.

```bash
VAULT_URI=http://localhost:8200
VAULT_TOKEN=<Initial Root Token>
VAULT_MOUNT_PATH=secret
```

## 3. Run the sample application

```bash
cd samples/24-vault/single-secret
set -a && source .env && set +a && mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Repeat for `multiple-secrets` and `secret-rotation`.

## 4. Configuration reference

| Variable | Default | Required | Description |
|----------|---------|----------|-------------|
| `VAULT_MOUNT_PATH` | `secret` | No | KV v2 mount path |
| `VAULT_READ_PATH_TEMPLATE` | `/v1/%s/data/%s` | No | Vault KV v2 read endpoint template |
| `VAULT_TOKEN` | — | Yes | Initial Root Token from `vault operator init` |
| `VAULT_URI` | `http://localhost:8200` | No | Vault server address |

Secrets to load are configured in `application.yml`:

```yaml
app:
  vault:
    secrets:
      - path: "spring-boot-tutorial"
        key: "api-secret"
      - path: "spring-boot-tutorial"
        key: "db-password"
```

Add or remove entries from the list to control which secrets are loaded at startup. If any secret fails to load, the application refuses to start.

## 5. Run tests

Tests use `MockRestServiceServer` and do not require a running Vault instance:

```bash
mvn test
```

Each Vault sub-project includes an `I18nConsistencyTest` that validates locale key parity, placeholder arity, unused keys, and missing keys used in code.

## 6. Stop Vault

```bash
cd samples/infrastructure/vault
docker compose down
```

The next start still requires unseal. Data remains in `volumes/file` unless you delete that directory.

If you lose the unseal key, you cannot unseal. Delete `volumes/file`, start again, and update `VAULT_TOKEN` in the sample `.env` files.

### Troubleshooting

Infrastructure failures (sealed after reboot, lost keys, port 8200, bind-mount permissions) are in [`samples/infrastructure/vault/README.md`](../../../samples/infrastructure/vault/README.md).

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
