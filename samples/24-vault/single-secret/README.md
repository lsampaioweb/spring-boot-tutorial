# 24 — Vault single-secret

## Before you start

Complete Vault init/unseal/seed from
[samples/infrastructure/vault/README.md](../../infrastructure/vault/README.md).
Export `VAULT_TOKEN` from that runbook.

## Run

```bash
cd samples/24-vault/single-secret
export VAULT_TOKEN=...   # from vault init
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8091`.

## Try it

```bash
curl -i http://localhost:8091/actuator/health
```

Expected: `HTTP/1.1 200`. Startup fails clearly if secrets are missing.
