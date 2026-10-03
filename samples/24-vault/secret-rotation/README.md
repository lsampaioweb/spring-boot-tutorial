# 24 — Vault secret-rotation

Reads rotating secrets on a short interval (see sample YAML / logs).

## Run

```bash
cd samples/24-vault/secret-rotation
export VAULT_TOKEN=...
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8093`. Watch logs for rotation updates; health at
`http://localhost:8093/actuator/health`.
