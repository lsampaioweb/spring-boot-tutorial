# 24 — Vault multiple-secrets

## Run

```bash
cd samples/24-vault/multiple-secrets
export VAULT_TOKEN=...
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8092`. Health: `curl -i http://localhost:8092/actuator/health`
