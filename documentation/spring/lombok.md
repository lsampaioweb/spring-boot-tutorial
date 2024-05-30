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

2. @ToString.

    Generate a `toString()` method that includes all fields of the class.
    ```java
    import lombok.ToString;

    @ToString
    public class User {
      private String name;
      private int age;
    }
    ```

3. @EqualsAndHashCode.

    Generate `equals()` and `hashCode()` methods based on the fields of the class.
    ```java
    import lombok.EqualsAndHashCode;

    @EqualsAndHashCode
    public class User {
      private String name;
      private int age;
    }
    ```

4. @NoArgsConstructor, @AllArgsConstructor, @RequiredArgsConstructor.

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

5. @Data.

    Combines `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, and `@RequiredArgsConstructor` into a single annotation.
    ```java
    import lombok.Data;

    @Data
    public class User {
      private String name;
      private int age;
    }
    ```

6. @Value.

    Generates an immutable class. It is a variant of `@Data` that creates all fields as `private final` and does not generate setters.
    ```java
    import lombok.Value;

    @Value
    public class User {
      private String name;
      private int age;
    }
    ```

7. @Builder.

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

8. @Slf4j and other logging annotations.

    Generate a log field using various logging frameworks (e.g., SLF4J, Log4j, Log4j2).
    ```java
    import lombok.extern.slf4j.Slf4j;

    @Slf4j
    public class UserService {
      public void logMessage() {
        log.info("This is a log message");
      }
    }
    ```

#
### Created by:

1. Luciano Sampaio.
