# DevTools

Enable Spring Boot DevTools so classpath changes restart the app during local work.

Working sample: [`samples/02-devtools`](../../../samples/02-devtools). Runbook:
[`samples/02-devtools/README.md`](../../../samples/02-devtools/README.md).

## Before you start

- Previous: [Maven Commands](../../maven/pom.md) — `samples/01-pom`
- Java 25, Maven 3.9+
- Time: ~10 minutes

## Why this exists

Without DevTools, every Java edit means stop and restart Maven yourself. DevTools
watches the classpath and restarts the JVM process when classes change, so the
feedback loop stays short. This sample is a non-web app on purpose: you only need
to see the restart in the console.

## What you will see

- App starts with no HTTP port
- After you edit and save a class under `src/main/java`, the console shows a DevTools restart

## Run

```bash
cd samples/02-devtools
mvn spring-boot:run
```

DevTools restart is enabled in `application.yml`:

```yml
spring:
  devtools:
    restart:
      enabled: true
```

Dependency (runtime, optional):

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
```

## Try it

1. Leave `mvn spring-boot:run` running.
1. Change a comment or log string in `DevtoolsApplication` and save.

Expected: console lines about restarting / relaunching the application (no curl — this sample has no HTTP API).

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `DevtoolsApplication` | Entry point |
| `application.yml` | `spring.devtools.restart.enabled` |
| `pom.xml` | `spring-boot-devtools` dependency |

## Tests

```bash
cd samples/02-devtools && mvn test
```

Loads the Spring context (`contextLoads`).

## Stop

`Ctrl+C`.

## Previous

[Maven commands](../../maven/pom.md).

## Next
[Profile](profile.md) — `samples/03-profiles`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
