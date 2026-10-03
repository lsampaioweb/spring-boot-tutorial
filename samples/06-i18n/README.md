# 06 — i18n

Resolves `MessageSource` greetings for English and Portuguese on startup.

## Before you start

- Previous: [05-lombok](../05-lombok/README.md)
- Topic: [documentation/spring/intermediate/i18n.md](../../documentation/spring/intermediate/i18n.md)

## Run

```bash
cd samples/06-i18n
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## What you will see

- Log lines with localized messages from `i18n/messages*.properties`

## Tests

```bash
mvn test
```

## Next

[07-actuator](../07-actuator/README.md)
