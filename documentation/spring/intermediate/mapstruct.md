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
  <lombok-mapstruct-binding.version>0.2.0</lombok-mapstruct-binding.version>
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
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
          </path>
          <path>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok-mapstruct-binding</artifactId>
            <version>${lombok-mapstruct-binding.version}</version>
          </path>
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

Always include `lombok-mapstruct-binding` when the module also uses Lombok, even if
DTOs are records today. That keeps MapStruct working if you later add Lombok
accessors on mapped types.
### Basic mapper

```java
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface UserMapper {

  UserResponse toResponse(User user);

  @Mapping(target = "id", constant = "0L")
  User toNewEntity(UserRequest request);

  @Mapping(target = "id", source = "id")
  User toEntity(Long id, UserRequest request);
}
```

What this means:

- `@Mapper`: marks this interface for MapStruct code generation.
- `componentModel = "spring"`: generated implementation becomes a Spring bean, so you can inject it.
- `unmappedTargetPolicy = ERROR`: fail the build if a destination field is forgotten.
- `toResponse`: same field names are mapped automatically.
- `toNewEntity`: `constant = "0L"` fills a `Long` id (use `"0"` only for `int`/`Integer`).
- `toEntity(Long id, UserRequest request)`: two sources; `id` comes from the method argument, `name` and `email` come from the request.

### Understanding @Mapping

Example:

```java
@Mapping(target = "id", constant = "0L")
User toNewEntity(UserRequest request);
```

Meaning:

- `target = "id"`: map to the `id` field in the destination object.
- `constant = "0L"`: always assign `0L` to a `Long` destination `id`, ignoring source input.

The MapStruct sample then replaces that placeholder id in the service before storing the user. The HTTP Client sample sends `0L` to the remote create API.

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

- `samples/11-mapstruct`
- `samples/12-http-client`
