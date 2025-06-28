# Create a Spring Boot Project with Maven

This guide explains how to create a Spring Boot project using `VS Code` or CLI on `Ubuntu`, targeted at non-beginner Java developers.


### Using VS Code
1. Create a new Java project in `VS Code`.

    ![01-new-java-project](../images/spring-project/01-new-java-project.png "01-new-java-project")

1. Select Spring Boot project type.

    ![02-spring-boot-project](../images/spring-project/02-spring-boot-project.png "02-spring-boot-project")

1. Select Maven as the dependency manager.

    ![03-maven-dependency-manager](../images/spring-project/03-maven-dependency-manager.png "03-maven-dependency-manager")

1. Select the Spring Boot version.
    ![04-spring-boot-version](../images/spring-project/04-spring-boot-version.png "04-spring-boot-version")

1. Select Java as the project language.

    ![05-project-language-java](../images/spring-project/05-project-language-java.png "05-project-language-java")

1. Set the Group ID to `com.learning` to uniquely identify the project.

    ![06-pom-group-id](../images/spring-project/06-pom-group-id.png "06-pom-group-id")

1. Set the Artifact ID to `pom` as the project identifier.

    ![07-pom-artifact-id](../images/spring-project/07-pom-artifact-id.png "07-pom-artifact-id")

1. Select JAR as the packaging type.

    ![08-packaging-type-jar](../images/spring-project/08-packaging-type-jar.png "08-packaging-type-jar")

1. Select Java version (e.g., 21, check latest with `apt search openjdk`).

    ![09-java-version](../images/spring-project/09-java-version.png "09-java-version")

1. Select dependencies (e.g., `spring-boot-starter-web`).

    ![10-spring-boot-dependencies](../images/spring-project/10-spring-boot-dependencies.png "10-spring-boot-dependencies")

1. Choose the project save path.

    ![11-project-path](../images/spring-project/11-project-path.png "11-project-path")

1. Clean up unnecessary files after creation, assuming Maven is installed.

    ![12-project-files-initial](../images/spring-project/12-project-files-initial.png "12-project-files-initial")
    ![13-project-files-clean](../images/spring-project/13-project-files-clean.png "13-project-files-clean")

### Using CLI
1. Generate the project using Spring Initializr CLI.
    ```bash
    curl https://start.spring.io/starter.zip -d dependencies=web -d type=maven-project -d javaVersion=21 -d groupId=com.learning -d artifactId=pom -o pom.zip
    ```

1. Extract and navigate to the project directory.
    ```bash
    unzip pom.zip -d 01-pom
    cd 01-pom
    ```

1. Run the Spring Boot application.
    ```bash
    mvn spring-boot:run
    ```

1. Troubleshoot common issues.
    ```bash
    echo $JAVA_HOME
    mvn clean install
    apt-cache search openjdk
    ```

[Go Back](../../README.md)

#
### Created by:
1. Luciano Sampaio.
