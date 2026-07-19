<!-- filepath: documentation/spring/integrations/vault.md -->

# Spring Boot + HashiCorp Vault

This tutorial shows how to read secrets from HashiCorp Vault using the sample applications in `samples/21-vault`. On startup, the `VaultSecretRegistry` loads all configured secrets into an in-memory cache. No further requests are made to Vault after startup.

The Vault sample is split into three focused sub-projects:

| Sub-project | Path | Demonstrates |
|-------------|------|--------------|
| `single-secret` | `samples/21-vault/single-secret` | Load a single secret at startup |
| `multiple-secrets` | `samples/21-vault/multiple-secrets` | Load a list of secrets defined in `application.yml` |
| `secret-rotation` | `samples/21-vault/secret-rotation` | Reload secrets at runtime without restarting the application |

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

Go to the sub-project folder you want to run (e.g. `multiple-secrets`):

```bash
cd samples/21-vault/multiple-secrets
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

Repeat from step 3 for the other sub-projects (`single-secret`, `secret-rotation`) to explore each pattern.

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

### i18n Quality Checks Included
Each Vault sub-project now includes an `I18nConsistencyTest` that validates:

1. Locale key parity between `messages.properties` and `messages_pt_BR.properties`.
1. Placeholder arity parity for each shared key (for example `{0}`, `{1}`).
1. Locale keys not used in code (fails when a key exists in bundles but is unused).
1. Missing locale keys used by code constants (fails when code references `log.*` or `error.*` keys that are not present in bundles).

These checks fail fast in CI and keep locale files synchronized with actual key usage in `VaultSecretRegistry` and `VaultSecretService`.

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
