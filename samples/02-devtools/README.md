# 02 — DevTools

Enables Spring Boot DevTools automatic restart during local development.

## Before you start

- Previous: [01-pom](../01-pom/README.md)
- Topic: [documentation/spring/basic/devtools.md](../../documentation/spring/basic/devtools.md)

## Run

```bash
cd samples/02-devtools
mvn spring-boot:run
```

## What you will see

- App starts (no HTTP port)
- Change a class under `src/main/java` and save; DevTools triggers a restart in the console

## Tests

```bash
mvn test
```

## Next

[03-profiles](../03-profiles/README.md)
