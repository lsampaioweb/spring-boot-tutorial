# Vault

Load application secrets from HashiCorp Vault at startup (and optionally refresh them).

Working samples: [`samples/24-vault`](../../../samples/24-vault)
([catalog README](../../../samples/24-vault/README.md)).

| Module | Port | Runbook |
| --- | --- | --- |
| `single-secret` | **8091** | [`single-secret/README.md`](../../../samples/24-vault/single-secret/README.md) |
| `multiple-secrets` | **8092** | [`multiple-secrets/README.md`](../../../samples/24-vault/multiple-secrets/README.md) |
| `secret-rotation` | **8093** | [`secret-rotation/README.md`](../../../samples/24-vault/secret-rotation/README.md) |

Infrastructure: [`samples/infrastructure/vault/README.md`](../../../samples/infrastructure/vault/README.md).

There is **no** domain REST API — verify with Actuator health after secrets load.

## Before you start

- Previous: [Redis](redis.md) — `samples/23-redis`
- Docker/Podman, Java 25, Maven 3.9+
- Complete Vault init / unseal / seed from the infra runbook
- Time: ~30 minutes (first Vault bootstrap dominates)

## Why this exists

Passwords and API keys should not live in `application.yml`. These samples use
Spring Cloud Vault to read KV v2 path `secret/spring-boot-tutorial` into an
in-memory registry. `secret-rotation` reloads on a schedule; the other two load
once at startup.

## What you will see

- Vault on **8200**
- App starts only if required keys (`api-secret`, `db-password`) are present
- `GET /actuator/health` → **200** on the module port
- Startup logs such as `Vault secrets loaded successfully`

## Run

### 1) Start and seed Vault

Follow [`samples/infrastructure/vault/README.md`](../../../samples/infrastructure/vault/README.md).
Short path:

```bash
cd samples/infrastructure/vault
cp .env.example .env
docker compose up -d
```

First start: `vault operator init`, save Unseal Key + Initial Root Token, then
unseal, enable KV v2 at `secret`, and put:

```bash
vault kv put secret/spring-boot-tutorial \
  api-secret=my-api-secret \
  db-password=my-db-password
```

(Use `docker exec …` as in the infra README.)

### 2) Run a module

```bash
cd samples/24-vault/single-secret
cp .env.example .env
# set VAULT_TOKEN to the Initial Root Token
set -a && source .env && set +a
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Env vars used by the sample YAML:

| Variable | Required | Notes |
| --- | --- | --- |
| `VAULT_TOKEN` | Yes | Root/bootstrap token from init |
| `VAULT_URI` | No | Development default `http://localhost:8200` |
| `VAULT_KV_BACKEND` | No | Default `secret` |

Repeat for `multiple-secrets` (8092) or `secret-rotation` (8093).

## Try it

```bash
curl -i http://localhost:8091/actuator/health
```

Expected: `HTTP/1.1 200` with an UP health payload when Vault is reachable and
secrets loaded. Startup fails with a clear error if a required secret is missing.

For rotation, watch logs every ~15s for refresh lines while
`samples/24-vault/secret-rotation` runs on **8093**.

## How the sample is shaped

| Concern | Location |
| --- | --- |
| Secret cache | `VaultSecretRegistry` (rotation variant refreshes on a schedule) |
| Required keys | `app.vault.required-secrets` / secrets list in `application.yml` |
| Ports | 8091 / 8092 / 8093 per module |

## Tests

```bash
cd samples/24-vault/single-secret && mvn test
```

Tests mock Vault HTTP (`MockRestServiceServer`); a live Vault is not required for
`mvn test`.

## Stop

`Ctrl+C` for the app, then:

```bash
cd samples/infrastructure/vault
docker compose down
```

Next start still needs unseal. Losing the unseal key means wiping `volumes/file`
and re-init (update `VAULT_TOKEN` afterward).

## Next

[WebSocket](../advanced/websocket.md) — `samples/25-websocket`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
