# Spring Boot Tutorial Project - AI Assistant Guidelines
# This file contains specific instructions for AI assistants working on this educational Spring Boot project.

## Project Overview
- **Type**: Educational Spring Boot tutorial with progressive learning modules.
- **Target Audience**: Developers learning Spring Boot from zero to hero.
- **Structure**: Sequential, isolated projects (01-pom, 02-devtools, etc.) each focusing on one concept.
- **Philosophy**: Each project should be self-contained but may build upon concepts from previous projects.

## Architecture & Design Principles
- **Educational Focus**: Prioritize clarity and teaching value over advanced abstractions.
- **Progressive Complexity**: Start simple, gradually introduce more concepts.
- **Isolation Principle**: Each numbered project should work independently.
- **Code Reuse Philosophy**: Repeat setup code (like Lombok, logging) in each project for learning reinforcement.
- **No Common Modules**: Avoid shared libraries to keep projects self-contained.

## Documentation Standards

### Markdown Style Preferences
- Use `1.` for ALL numbered lists (let Markdown auto-increment).
- ONE command per code block with description above.
- Always include filepath comments in code blocks.
- Use `<!-- filepath: ... -->` comments at top of markdown files.
- End each documentation file with navigation and author signature.

### Documentation Structure
```markdown
# Title (descriptive, not just feature name)

Brief introduction explaining what this teaches.

1. Step description.
    ```language
    command or code
    ```

1. Next step description.
    ```language
    next command
    ```

**Notes:** (if applicable)
- Key points
- Best practices
- Troubleshooting tips

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
```

## Code Style & Conventions

### Java Code Standards
- **Package Structure**: `com.learning.{projectname}.{feature}`.
- **Class Naming**: Descriptive names (UserController, UserService, UserRepository).
- **Lombok Usage**: Prefer `@Data`, `@Slf4j`, `@AllArgsConstructor`, `@NoArgsConstructor`.
- **Constructor Injection**: Always use constructor injection, not field injection.
- **REST Controllers**: Use ResponseEntity for proper HTTP status codes.
- **Service Layer**: Keep business logic in service classes, not controllers.

### Spring Boot Patterns
- **Configuration**: Use `application.yml` (not .properties).
- **Profiles**: Use profile-specific files (application-{profile}.yml).
- **Logging**: Use SLF4J with Lombok's `@Slf4j`.
- **Error Handling**: Centralized exception handling with `@RestControllerAdvice`.
- **Validation**: Use Bean Validation annotations.
- **REST API**: Follow RESTful conventions with proper HTTP methods and status codes.

### Project Structure (per numbered project)
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

## Technology Stack & Dependencies

### Core Dependencies (per project needs)
- Spring Boot 3.x (latest stable).
- Java 21.
- Lombok (scope: provided).
- Spring Boot Starters as needed.
- Maven (not Gradle).

### Dependency Management
- Use Spring Boot's dependency management (no version numbers for Spring deps).
- Scope Lombok as `provided`.
- Use `spring-boot-starter-*` dependencies.
- Keep dependencies minimal per project.

## Error Handling & Logging

### Logging Preferences
- Use SLF4J with Logback.
- Structured logging with profile-specific configurations.
- Log files in `logs/` directory (when applicable).
- Message internationalization for logs when i18n is introduced.

### Exception Handling
- Custom exceptions extending RuntimeException.
- Centralized handling with @RestControllerAdvice.
- Internationalized error messages.
- Include stack traces in development profiles only.

## Testing Guidelines
- Basic `@SpringBootTest` context loading tests.
- Integration tests for REST endpoints.
- Keep tests simple and educational.
- Focus on demonstrating testing concepts, not comprehensive coverage.

## Internationalization (i18n)
- Message files in `resources/i18n/`.
- Use MessageSource for all user-facing text.
- Support for `en` (default) and `pt-BR`.
- Apply i18n consistently after project 07.

## Security Considerations
- Environment variables for sensitive data (passwords, keys).
- Use secret management tools (secret-tool) for local development.
- HTTPS configuration with proper TLS versions.
- Never commit secrets to repository.

## Performance & Best Practices
- Virtual threads for I/O operations (when introduced).
- Connection pooling configuration.
- Proper HTTP status codes and caching headers.
- Pagination for large datasets.

## Code Templates

### Basic Controller Template
```java
package com.learning.{project}.{feature};

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/{resource}")
@Slf4j
public class {Feature}Controller {

    private final {Feature}Service service;

    public {Feature}Controller({Feature}Service service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<{Entity}>> findAll() {
        // Implementation
    }
}
```

### Basic Service Template
```java
package com.learning.{project}.{feature};

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class {Feature}Service {

    // Constructor injection for dependencies

    // Business logic methods
}
```

### Application.yml Template
```yaml
spring:
  application:
    name: "{project-name}"
  profiles:
    active: "development"

# Feature-specific configuration
```

## File Organization Rules
- One concept per numbered project.
- Keep project names short and descriptive.
- Use consistent naming: XX-{topic-name}.
- Documentation should mirror project structure.
- Group related documentation in appropriate folders.

## AI Assistant Instructions
- Always consider the educational value of suggestions.
- Prefer explicit, clear code over clever abstractions.
- Maintain consistency with existing project patterns.
- When suggesting improvements, explain the educational benefit.
- Consider the target audience (Spring Boot learners).
- Always provide complete, working examples.
- Include relevant documentation updates when changing code.
- Follow the progressive learning approach (don't introduce advanced concepts too early).

## Review Checklist
- [ ] Code follows project naming conventions.
- [ ] Documentation uses correct markdown format.
- [ ] One command per code block with descriptions.
- [ ] Proper error handling and logging.
- [ ] Consistent with previous project patterns.
- [ ] Educational value is clear.
- [ ] All necessary dependencies included.
- [ ] Tests are appropriate for the concept being taught.
