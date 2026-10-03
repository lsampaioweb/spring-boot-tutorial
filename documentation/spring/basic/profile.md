# Profile

Activate environment-specific YAML and print `app.environment` for the active profile.

Working sample: [`samples/03-profiles`](../../../samples/03-profiles). Runbook:
[`samples/03-profiles/README.md`](../../../samples/03-profiles/README.md).

## Before you start

- Previous: [DevTools](devtools.md) — `samples/02-devtools`
- Java 25, Maven 3.9+
- Time: ~10 minutes

## Why this exists

The same app often needs different settings for development and production. Spring
profiles load `application-{profile}.yml` on top of the base file. This sample
only toggles `app.environment` — no database or other infra fiction.

## What you will see

Stdout from a `CommandLineRunner`:

- `Active profiles: development` and `app.environment: development`, or
- `Active profiles: production` and `app.environment: production`

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

You can also set `SPRING_PROFILES_ACTIVE=development` (or `production`) in the shell.

## Try it

Run once with `development`, then once with the default production profile.

Expected stdout (development):

```text
Active profiles: development
app.environment: development
```

Expected stdout (production):

```text
Active profiles: production
app.environment: production
```

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `ProfilesApplication` | `CommandLineRunner` that prints profile + `app.environment` |
| `application.yml` | Default `spring.profiles.active: production`, base `app.environment` |
| `application-development.yml` | `app.environment: development` |
| `application-production.yml` | `app.environment: production` |

## Tests

```bash
cd samples/03-profiles && mvn test
```

Loads the Spring context.

## Stop

`Ctrl+C` if the process is still running.

## Next

[Logs](logs.md) — `samples/04-logs`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
