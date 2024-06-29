Spring Boot profiles provide a way to segregate parts of your application configuration and make it available only in certain environments. For example, you can have different configurations for development, testing, and production environments.

1. Define Profile-Specific Configuration Files.

    You can define multiple configuration files for different profiles. The default configuration file is `application.yml`. You can create additional configuration files for each profile:

    - `application.yml`
    - `application-development.yml`
    - `application-production.yml`

    **Common settings for all profiles can be added to the default file.**

    `application.yml`:
    ```yml
    spring:
      application:
        name: "learning-about-profiles"
    ```

    `application-development.yml`:
    ```yml
    logging:
      level:
        root: "DEBUG"
    spring:
      datasource:
        url: "jdbc:mysql://dev-server:3306/dev-db"
        username: "dev-user"
        password: "******"
    ```

    `application-production.yml`:
    ```yml
    logging:
      level:
        root: "INFO"
    spring:
      datasource:
        url: "jdbc:mysql://prod-server:3306/prod-db"
        username: "prod-user"
        password: "******"
    ```

1. Activate a Profile.

    You can activate a profile in several ways:

    - Using `application.yml`:
    ```yml
    spring:
      profiles:
        # active: "default"
        active: "development"
        # active: "production"
    ```

    - Using Environment Variable:
    ```bash
    export SPRING_PROFILES_ACTIVE=development
    # or
    export SPRING_PROFILES_ACTIVE=production
    ```

    - Using Command Line:
    ```bash
    java -jar -Dspring.profiles.active=development myapp.jar
    # or
    java -jar -Dspring.profiles.active=production myapp.jar
    ```

1. Setup multiple Profiles in VSCode.

    This setup allows you to easily switch between different profiles and run your Spring Boot application with the desired configuration directly from VSCode.

    1. Open your project in VSCode.

    1. Create a folder named `.vscode` in the root of your project if it doesn't already exist.

    1. Inside the this folder, create a new file named `launch.json`.

    1. Add the following configuration to the `launch.json` file:
    ```json
    {
      "version": "0.2.0",
      "configurations": [
        {
          "type": "java",
          "name": "Profiles - Development",
          "request": "launch",
          "mainClass": "com.example.YourSpringBootApplication",
          "args": "--spring.profiles.active=dev",
          "projectName": "your-project-name"
        },
        {
          "type": "java",
          "name": "Profiles - Production",
          "request": "launch",
          "mainClass": "com.example.YourSpringBootApplication",
          "args": "--spring.profiles.active=prod",
          "projectName": "your-project-name"
        }
      ]
    }
    ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
