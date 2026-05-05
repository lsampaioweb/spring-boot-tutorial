# Spring Boot Tutorial Project - AI Assistant Guidelines

## Project Overview
- **Type**: Educational Spring Boot tutorial with progressive learning modules.
- **Target Audience**: Developers learning Spring Boot from zero to hero.
- **Structure**: Sequential, isolated projects (`samples/XX-{topic}/`) each focusing on one concept.
- **Philosophy**: Each project should be self-contained and may build upon concepts from previous projects.
- **Maintenance reality**: Samples are intentionally independent, so version upgrades usually require touching multiple `pom.xml` files.

> Spring Boot coding conventions (Maven, Java 21, constructor injection, application.yml, ResponseEntity, Lombok, etc.) are defined in the shared skill. This file contains only rules specific to this tutorial project.

## AI Customization Roles
- Keep this file for always-on, repository-wide rules.
- Use the `spring-boot` skill for task-specific Spring Boot implementation guidance.
- Avoid duplicating the same coding convention in both places.
- When asked to upgrade or refresh the tutorial samples, prefer the repository maintenance workflow described in `documentation/maven/upgrade.md`.

## Educational Principles
- Prioritize clarity and teaching value over advanced abstractions.
- Start simple, gradually introduce more concepts — do not introduce advanced patterns before their designated project.
- Repeat setup code (like Lombok, logging) in each project for learning reinforcement.
- Avoid shared libraries to keep projects self-contained.
- Always provide complete, working examples and explain the educational benefit of any suggestion.

## Project Structure
```
samples/XX-{topic}/
├── src/
│   ├── main/
│   │   ├── java/com/learning/{topic}/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── templates/ (if needed)
│   └── test/
├── pom.xml
└── README.md (if project-specific docs needed)
```
- Package structure: `com.learning.{projectname}.{feature}`.
- One concept per numbered project; use consistent naming: `XX-{topic-name}`.
- Keep dependencies minimal per project — only what that sample teaches.
- Preserve self-contained samples unless the user explicitly asks for shared parents, shared libraries, or repo-wide restructuring.

## Documentation Standards
- Use `1.` for ALL numbered lists (let Markdown auto-increment).
- ONE command per code block with a description above it.
- Always include `<!-- filepath: ... -->` comments at the top of markdown files.
- End each documentation file with navigation and author signature:

```markdown
[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
```

## Internationalization (i18n)
- Message files in `resources/i18n/`.
- Use `MessageSource` for all user-facing text.
- Support `en` (default) and `pt-BR`.
- Apply i18n consistently **after project 07** only — do not introduce it earlier.

## Testing Guidelines
- Keep tests simple and educational — demonstrate the concept, not exhaustive coverage.
- Basic `@SpringBootTest` context loading tests are sufficient for early projects.
- Introduce integration tests for REST endpoints when that topic is reached.

## Review Checklist
- [ ] One concept per numbered project; no shared modules introduced.
- [ ] Package follows `com.learning.{topic}` structure.
- [ ] Documentation uses correct markdown format (1. lists, one command per block, filepath comment).
- [ ] Navigation footer and author signature present.
- [ ] Educational value is clear; no advanced abstractions introduced prematurely.
- [ ] i18n only applied from project 07 onward.
- [ ] Tests demonstrate the concept being taught.
- [ ] All Spring Boot coding conventions from the shared skill are followed.
