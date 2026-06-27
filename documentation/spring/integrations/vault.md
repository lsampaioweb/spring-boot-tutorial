<!-- filepath: documentation/spring/integrations/vault.md -->

# Spring Boot + HashiCorp Vault

This tutorial shows how to read secrets from HashiCorp Vault using the sample application in `samples/21-vault`. On startup, the `VaultSecretRegistry` loads all configured secrets into an in-memory cache. No further requests are made to Vault after startup.

## Prerequisites
1. Podman with Podman Compose
1. Java 25
1. Maven 3.9+

## 1. Start Vault Infrastructure

```bash
cd samples/infrastructure/vault
```

```bash
podman compose up -d
```

Check if Vault is healthy:

```bash
podman compose ps
```

## 2. Seed Secrets in Vault
Set the required secrets in the KV v2 mount (`secret`) under the path `spring-boot-tutorial`:

```bash
podman exec tutorial-vault vault kv put secret/spring-boot-tutorial api-secret=my-api-secret db-password=my-db-password
```

## 3. Configure Environment
Go to the sample folder:

```bash
cd samples/21-vault
```

Edit `.env` and uncomment `VAULT_TOKEN`:

```bash
VAULT_TOKEN=tutorial-root-token
```

## 4. Run the Sample Application
Load the environment variables and start with the development profile:

```bash
set -a && source .env && set +a && mvn spring-boot:run -Dspring-boot.run.profiles=development
```

On startup, the application reads all secrets listed under `app.vault.secrets` in `application.yml` and caches them. Check `logs/vault.log` to confirm successful loading.

## 5. Configuration Reference
Environment variables (set in `.env` or exported before running):

| Variable | Default | Required | Description |
|----------|---------|----------|-------------|
| `VAULT_MOUNT_PATH` | `secret` | No | KV v2 mount path |
| `VAULT_READ_PATH_TEMPLATE` | `/v1/%s/data/%s` | No | Vault KV v2 read endpoint template |
| `VAULT_TOKEN` | — | Yes | Vault authentication token |
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

## 6. Run Tests
Tests use `MockRestServiceServer` and do not require a running Vault instance:

```bash
mvn test
```

## 7. Stop Vault Infrastructure

```bash
cd samples/infrastructure/vault
```

```bash
podman compose down
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
