# 04 — Logs

Demonstrates Logback levels (TRACE through ERROR) on startup.

## Before you start

- Previous: [03-profiles](../03-profiles/README.md)
- Topic: [documentation/spring/basic/logs.md](../../documentation/spring/basic/logs.md)

## Run

```bash
cd samples/04-logs
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Use the `development` profile so you see console output. The default `production`
profile may write mainly to files under `logs/`.

## What you will see

- Startup log lines at several levels from the sample `CommandLineRunner`

## Tests

```bash
mvn test
```

## Next

[05-lombok](../05-lombok/README.md)
