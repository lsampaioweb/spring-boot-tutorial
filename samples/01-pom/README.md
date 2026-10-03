# 01 — POM

Minimal Spring Boot app that shows the parent POM and first-project layout.

## Before you start

- Java 25, Maven 3.9+
- Topic: [documentation/maven/pom.md](../../documentation/maven/pom.md)

## Run

```bash
cd samples/01-pom
mvn compile
mvn test
mvn spring-boot:run
```

## What you will see

- `BUILD SUCCESS` for compile/test
- Application starts with no web server; stop with `Ctrl+C`

## Tests

```bash
mvn test
```

## Next

[02-devtools](../02-devtools/README.md)
