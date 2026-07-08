## MapStruct

MapStruct is a Java annotation processor that generates object-to-object mapping code at compile time.

It is useful when you need to convert between:

- Request DTO -> Domain object
- Domain object -> Response DTO

### Why use it

1. Less boilerplate code for repetitive field mapping.
1. Compile-time validation for mapping issues.
1. Generated code is plain Java and usually fast.

### Add dependencies

In `pom.xml`, add `mapstruct` and the annotation processor:

```xml
<properties>
  <mapstruct.version>1.6.3</mapstruct.version>
</properties>

<dependencies>
  <dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>${mapstruct.version}</version>
  </dependency>
</dependencies>

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <annotationProcessorPaths>
          <path>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>${mapstruct.version}</version>
          </path>
        </annotationProcessorPaths>
      </configuration>
    </plugin>
  </plugins>
</build>
```

### Basic mapper

```java
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {

  UserResponse toResponse(User user);

  User toEntity(UserRequest request);
}
```

What this means:

- `@Mapper`: marks this interface for MapStruct code generation.
- `componentModel = "spring"`: generated implementation becomes a Spring bean, so you can inject it.
- `toResponse` and `toEntity`: MapStruct generates these methods by matching fields with the same names.

### Understanding @Mapping

Example:

```java
@Mapping(target = "id", constant = "0")
User toEntity(UserRequest request);
```

Meaning:

- `target = "id"`: map to the `id` field in the destination object.
- `constant = "0"`: always assign `0` to destination `id`, ignoring source input.

In this tutorial sample, it preserves the previous behavior where new users are created with `id = 0` before persistence logic assigns a final id.

### Other common @Mapping options

1. Ignore a field:

```java
@Mapping(target = "id", ignore = true)
Product toEntity(ProductRequest request);
```

Use this when destination `id` should not come from request input.

1. Rename source field:

```java
@Mapping(source = "fullName", target = "name")
User toEntity(UserRequest request);
```

Use this when source and destination field names are different.

1. Fallback value when source is null:

```java
@Mapping(target = "status", defaultValue = "ACTIVE")
AccountResponse toResponse(Account account);
```

### Where generated code is placed

MapStruct generates implementations under:

- `target/generated-sources/annotations`

You normally do not edit generated classes manually.

### Troubleshooting

1. Error: `Unmapped target property`

Cause: destination has a field not mapped from source.

Fix options:

- Add an explicit mapping with `@Mapping(...)`.
- Ignore the field with `@Mapping(target = "field", ignore = true)`.

1. Mapper bean is not found by Spring

Cause: `componentModel = "spring"` is missing.

Fix:

- Add `@Mapper(componentModel = "spring")`.

### Samples using MapStruct in this project

- `samples/12-http-client`
- `samples/15-exception-handling`
