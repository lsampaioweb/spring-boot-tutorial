# 03 — Profiles

Prints profile-specific `app.environment` via a `CommandLineRunner`.

## Before you start

- Previous: [02-devtools](../02-devtools/README.md)
- Topic: [documentation/spring/basic/profile.md](../../documentation/spring/basic/profile.md)

## Run

Development:

```bash
cd samples/03-profiles
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Production (default in `application.yml` if you omit `-D`):

```bash
mvn spring-boot:run
```

## What you will see

- Log lines with the active profile and `app.environment` value from the matching `application-*.yml`

## Tests

```bash
mvn test
```

## Next

[04-logs](../04-logs/README.md)
