# 24 — Vault

Three modules that load secrets from HashiCorp Vault at startup (no domain REST API).

| Module | Port |
| --- | --- |
| [single-secret](single-secret/README.md) | `8091` |
| [multiple-secrets](multiple-secrets/README.md) | `8092` |
| [secret-rotation](secret-rotation/README.md) | `8093` |

Infra runbook (init/unseal/seed): [samples/infrastructure/vault/README.md](../infrastructure/vault/README.md)

Topic: [documentation/spring/integrations/vault.md](../../documentation/spring/integrations/vault.md)
