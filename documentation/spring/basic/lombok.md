Lombok is a Java library that provides annotations to simplify Java development by automating the generation of boilerplate code. Key features include automatic generation of getters, setters, equals, hashCode, and toString methods, as well as a facility for automatic resource management.

1. Add the dependency in your `pom.xml`.

    ```xml
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <scope>provided</scope>
    </dependency>
    ```

1. Exclude it from the final packaged application.

    This configuration ensures that the Lombok dependency, which is only needed at compile time, is not included in the final packaged application (JAR or WAR).
    ```xml
    <scope>provided</scope>
    ```

1. Annotation processing on Java 23+.

    Java 23 deprecated and Java 25 removed implicit annotation processor discovery from the classpath. Any project using Lombok on Java 23 or later requires an explicit `annotationProcessorPaths` entry in `maven-compiler-plugin`, otherwise `@Slf4j`, `@Data`, and other annotations produce "cannot find symbol" errors.

    Add this block to the `<build><plugins>` section of the `pom.xml`:

    ```xml
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <annotationProcessorPaths>
          <path>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
          </path>
        </annotationProcessorPaths>
      </configuration>
    </plugin>
    ```

    No version is needed; Spring Boot's parent POM manages it.

1. VS Code Integration.

    Install the Lombok extension using the UI.
      - Name: `Lombok Annotations Support for VS Code`

    or

    ```bash
    code --install-extension vscjava.vscode-lombok
    ```

1. @Getter and @Setter.

    Generate getter and setter methods for class fields.
    ```java
    import lombok.Getter;
    import lombok.Setter;

    @Getter @Setter
    public class User {
      private String name;
      private int age;
    }
    ```

    For Spring configuration binding (`@ConfigurationProperties`), prefer immutable Java records instead of mutable `@Getter`/`@Setter` classes.

1. @ToString.

    Generate a `toString()` method that includes all fields of the class.
    ```java
    import lombok.ToString;

    @ToString
    public class User {
      private String name;
      private int age;
    }
    ```

1. @EqualsAndHashCode.

    Generate `equals()` and `hashCode()` methods based on the fields of the class.
    ```java
    import lombok.EqualsAndHashCode;

    @EqualsAndHashCode
    public class User {
      private String name;
      private int age;
    }
    ```

1. @NoArgsConstructor, @AllArgsConstructor, @RequiredArgsConstructor.

    Generate constructors:
    - `@NoArgsConstructor`: No-argument constructor.
    - `@AllArgsConstructor`: Constructor with all fields.
    - `@RequiredArgsConstructor`: Constructor for final fields and fields marked with `@NonNull`.
    ```java
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;
    import lombok.RequiredArgsConstructor;
    import lombok.NonNull;

    @NoArgsConstructor
    @AllArgsConstructor
    @RequiredArgsConstructor
    public class User {
      private String name;
      @NonNull private int age;
    }
    ```

1. @Data.

    Combines `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, and `@RequiredArgsConstructor` into a single annotation.
    ```java
    import lombok.Data;

    @Data
    public class User {
      private String name;
      private int age;
    }
    ```

1. @Value.

    Generates an immutable class. It is a variant of `@Data` that creates all fields as `private final` and does not generate setters.
    ```java
    import lombok.Value;

    @Value
    public class User {
      private String name;
      private int age;
    }
    ```

1. @Builder.

    Implements the Builder pattern for object creation.
    ```java
    import lombok.Builder;

    @Builder
    public class User {
      private String name;
      private int age;
    }

    User user = User.builder()
                    .name("Luciano")
                    .age(41)
                    .build();
    ```

1. @Slf4j and other logging annotations.

    Generate a log field using various logging frameworks (e.g., SLF4J, Log4j, Log4j2).
    ```java
    import lombok.extern.slf4j.Slf4j;

    @Slf4j
    public class UserService {

      private static final String LOG_USER_MESSAGE = "log.user.message";

      public void logMessage() {
        log.info(logMessages.get(LOG_USER_MESSAGE));
      }
    }
    ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
