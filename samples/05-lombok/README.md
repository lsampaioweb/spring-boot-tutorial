# 05 — Lombok

Shows Lombok annotations (`@Data`, `@Builder`, `@Slf4j`, and friends) in a small demo.

## Before you start

- Previous: [04-logs](../04-logs/README.md)
- Topic: [documentation/spring/basic/lombok.md](../../documentation/spring/basic/lombok.md)
- IDE: Extension Pack for Java (annotation processing)

## Run

```bash
cd samples/05-lombok
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## What you will see

- Stdout demos from Lombok-generated accessors / builders / logging

## Tests

```bash
mvn test
```

## Next

[06-i18n](../06-i18n/README.md)
