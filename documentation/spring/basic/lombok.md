# Lombok

Generate getters, builders, and logger fields with Lombok annotations (Java 25-safe setup).

Working sample: [`samples/05-lombok`](../../../samples/05-lombok). Runbook:
[`samples/05-lombok/README.md`](../../../samples/05-lombok/README.md).

## Before you start

- Previous: [Logs](logs.md) — `samples/04-logs`
- Java 25, Maven 3.9+
- IDE: Extension Pack for Java + Lombok annotation support
- Time: ~15 minutes

## Why this exists

Entity and DTO demos still need accessors, `toString`, and constructors. Lombok
generates that at compile time. On Java 23+, you must declare Lombok on
`annotationProcessorPaths` or the compiler will not see generated members.

## What you will see

Stdout section headers and demo values from ordered `CommandLineRunner` beans:

- `Demo GetSet`, `Demo ToString`, `Demo EqualsAndHashCode`, …
- Example values such as `Luciano Sampaio`, `UserBuilder(name=Luciano, age=41)`
- `@Slf4j` lines from `UserService` when root logging is DEBUG (development)

## Run

```bash
cd samples/05-lombok
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

POM essentials (match the sample): `lombok` with `provided` scope, and
`maven-compiler-plugin` → `annotationProcessorPaths` → `lombok` (version from the
Spring Boot parent).

## Try it

Watch the console after startup.

Expected: seven demo blocks (`Demo GetSet` through `Demo UserService`) and SLF4J
messages from `UserService` (`This is a debug/info/error log message.`).

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `LombokApplication` | Ordered runners that exercise each annotation |
| `UserGetSet` | `@Getter` / `@Setter` |
| `UserToString` | `@ToString` |
| `UserEqualsAndHashCode` | `@EqualsAndHashCode` |
| `UserWithConstructors` | `@NoArgsConstructor`, `@AllArgsConstructor`, `@RequiredArgsConstructor` |
| `UserData` | `@Data` |
| `UserBuilder` | `@Builder` |
| `UserService` | `@Slf4j` |

Snippet that matches `UserGetSet` in the sample:

```java
@Getter
@Setter
public class UserGetSet {
  private String name;
  private int age;
}
```

## Tests

```bash
cd samples/05-lombok && mvn test
```

Loads the Spring context (compile proves annotation processing).

## Stop

`Ctrl+C`.

## Next

[i18n](../intermediate/i18n.md) — `samples/06-i18n`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
