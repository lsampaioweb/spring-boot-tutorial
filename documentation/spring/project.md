Create a Spring Boot project using VSCode.

1. Create a new Java project.

    ![01-new-java-project](../images/spring-project/01-new-java-project.png "01-new-java-project")

1. Select Spring Boot.

    ![02-spring-boot-project](../images/spring-project/02-spring-boot-project.png "02-spring-boot-project")

1. Select the dependency manager. We will use `Maven`.

    ![03-maven-dependency-manager](../images/spring-project/03-maven-dependency-manager.png "03-maven-dependency-manager")

1. Select the Spring Boot version. **I recommend using the latest**.

    ![04-spring-boot-version](../images/spring-project/04-spring-boot-version.png "04-spring-boot-version")

1. Select the project language. We will use `Java`.

    ![05-project-language-java](../images/spring-project/05-project-language-java.png "05-project-language-java")

1. Select the Group ID of the project.

    The `groupId` identifies your project uniquely across all projects.

    ![06-pom-group-id](../images/spring-project/06-pom-group-id.png "06-pom-group-id")

1. Select the Artifact ID of the project.

    The `artifactId` is the name of the project or module. It's a simple identifier for your project within the `groupId`.

    ![07-pom-artifact-id](../images/spring-project/07-pom-artifact-id.png "07-pom-artifact-id")

1. Select the packaging type. We will use `Jar`.

    ![08-packaging-type-jar](../images/spring-project/08-packaging-type-jar.png "08-packaging-type-jar")

1. Select the Java version. **I recommend using the latest**.

    ![09-java-version](../images/spring-project/09-java-version.png "09-java-version")

1. Select the dependencies that the project will use.

    ![10-spring-boot-dependencies](../images/spring-project/10-spring-boot-dependencies.png "10-spring-boot-dependencies")

1. Select where the project will be saved.

    ![11-project-path](../images/spring-project/11-project-path.png "11-project-path")

1. These are the files that will be created.

    We have already installed `Maven`, so we do not need the extra files and they can deleted:
    - .mvn
    - mvnw
    - mvnw.cmd

    ![12-project-files-initial](../images/spring-project/12-project-files-initial.png "12-project-files-initial")

    After deleting the extra files, the folder will look like this:

    ![13-project-files-clean](../images/spring-project/13-project-files-clean.png "13-project-files-clean")

#
### Created by:

1. Luciano Sampaio.
