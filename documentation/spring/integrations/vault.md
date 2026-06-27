<!-- filepath: documentation/spring/integrations/vault.md -->

# Spring Boot + HashiCorp Vault

This tutorial shows how to read a secret from HashiCorp Vault using the sample application in `samples/21-vault`.

## Prerequisites
1. Java 25
1. Maven 3.9+
1. Docker and Docker Compose

## 1. Start Vault Infrastructure
Run the infrastructure stack provided by this repository:

```bash
cd samples/infrastructure/vault
```

```bash
docker-compose up -d
```

Check if Vault is healthy:

```bash
docker-compose ps
```

## 2. Seed a Secret in Vault
Set a secret in the default KV v2 mount (`secret`) and path (`spring-boot-tutorial`):

```bash
docker exec tutorial-vault vault kv put secret/spring-boot-tutorial db-password=my-local-db-password
```

## 3. Run the Sample Application
Go to the sample:

```bash
cd samples/21-vault
```

Run with the development profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

On startup, the application reads `db-password` from Vault and writes a masked value to the log (`logs/vault.log`).

## 4. Configuration Reference
The sample supports these environment variables:

1. `VAULT_URI` (default: `http://localhost:8200`)
1. `VAULT_TOKEN` (default: `tutorial-root-token`)
1. `VAULT_MOUNT_PATH` (default: `secret`)
1. `VAULT_SECRET_PATH` (default: `spring-boot-tutorial`)
1. `VAULT_SECRET_KEY` (default: `db-password`)
1. `VAULT_STARTUP_READ_ENABLED` (default: `true`)

Run overriding defaults:

```bash
VAULT_URI=http://localhost:8200 VAULT_TOKEN=tutorial-root-token mvn spring-boot:run
```

## 5. Run Tests
Run the sample tests:

```bash
mvn test
```

## 6. Stop Vault Infrastructure

```bash
cd samples/infrastructure/vault
```

```bash
docker-compose down
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
