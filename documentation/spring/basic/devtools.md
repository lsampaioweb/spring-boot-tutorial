Spring Boot DevTools aim to reduce the development time by offering functionalities like automatic restarts, live reload, and configurations for faster feedback.

1. Add the dependency in your `pom.xml`.

    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <scope>runtime</scope>
      <optional>true</optional>
    </dependency>
    ```

1. Enable or disable Automatic Restarts.

    Devtools monitors classpath resources and automatically restarts the application whenever files are changed.

    You can configure it in the `application.properties` file:
    ```properties
    # default is true.
    spring.devtools.restart.enabled=true
    ```

#
### Created by:

1. Luciano Sampaio.
