# Logs

Emit TRACE–ERROR messages and route them with Logback profiles (console + file).

Working sample: [`samples/04-logs`](../../../samples/04-logs). Runbook:
[`samples/04-logs/README.md`](../../../samples/04-logs/README.md).

## Before you start

- Previous: [Profile](profile.md) — `samples/03-profiles`
- Java 25, Maven 3.9+
- Time: ~15 minutes

## Why this exists

Logging is already on the classpath via `spring-boot-starter`. What you configure
is *where* and *at which level* messages go. This sample wires
`classpath:log/logback-spring.xml` and shows level checks in a
`CommandLineRunner`.

## What you will see

With the **development** profile (console appender enabled):

- `A DEBUG Message`, `An INFO Message`, `A WARN Message`, `An ERROR Message`
- No `A TRACE Message` (trace is not enabled at root)
- A file under `./logs/` as well (async `File` appender)

With default **production**, Logback uses the file appender only (little or no console demo).

## Run

```bash
cd samples/04-logs
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Use `development` so Logback attaches the Console appender. Base YAML points at the real config:

```yml
logging:
  config: "classpath:log/logback-spring.xml"
  logback:
    rollingpolicy:
      max-file-size: "10MB"
      max-history: "7"
      total-size-cap: "1GB"
```

## Try it

Watch the console after startup.

Expected (development): DEBUG through ERROR lines from `LogsApplication`; TRACE absent.

Optional: open `./logs/logs.log` (app name `logs`) for the same messages on disk.

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `LogsApplication` | Logs TRACE–ERROR via SLF4J |
| `src/main/resources/log/logback-spring.xml` | Console + async file; `springProfile` for `development` vs `default \| production` |
| `application.yml` | `logging.config` + rolling policy keys |
| `application-development.yml` | `logging.level.root: DEBUG` |

Real Logback profile names in the sample:

- `development` → Console + File
- `default | production` → File only
- `debug` → DEBUG root with Console + File

## Tests

```bash
cd samples/04-logs && mvn test
```

Loads the Spring context.

## Stop

`Ctrl+C`.

## Next

[Lombok](lombok.md) — `samples/05-lombok`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
