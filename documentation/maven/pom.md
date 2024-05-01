Maven commands.

1. Compile the application to make sure everything is working.

    ```bash
    mvn compile
    ```

1. Update the version of the library/application in the pom after each new feature.

    ```bash
    mvn release:update-versions
    ```

    Before:

    ![02-version-0-0-1-snapshot](/spring-boot/tutorial/documentation/images/maven/02-version-0-0-1-snapshot.png "02-version-0-0-1-snapshot")

    Enter the new version:

    ![01-choose-version](/spring-boot/tutorial/documentation/images/maven/01-choose-version.png "01-choose-version")

    Maven build success message:

    ![04-version-0-0-2-snapshot-build](/spring-boot/tutorial/documentation/images/maven/04-version-0-0-2-snapshot-build.png "04-version-0-0-2-snapshot-build")

    After:

    ![03-version-0-0-2-snapshot](/spring-boot/tutorial/documentation/images/maven/03-version-0-0-2-snapshot.png "03-version-0-0-2-snapshot")

1. Prepare the next release.

    Add the `scm` tag with the repository url in the pom.xml.
    ```xml
    <project>
      ...
      <scm>
      <developerConnection>scm:git:https://github.com/lsampaioweb/spring-boot-tutorial.git</developerConnection>
      <tag>HEAD</tag>
      </scm>
      ...
    </project>
    ```

    Add the `maven-surefire-plugin` plugin.
    ```xml
    <build>
      <plugins>
        ...
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>3.2.5</version>
        </plugin>
              ...
      </plugins>
    </build>
    ```

    Create and publish a new version:

    ```bash
    mvn release:prepare
    ```

    Remove the `SNAPSHOT` from the version.

    ![05-new-release](/spring-boot/tutorial/documentation/images/maven/05-new-release.png "05-new-release")


    Commit and push the new release to the repository.

    ![06-commits](/spring-boot/tutorial/documentation/images/maven/06-commits.png "06-commits")

    Increase the version in the pom.xml.

    ![07-commits-new-version](/spring-boot/tutorial/documentation/images/maven/07-commits-new-version.png "07-commits-new-version")

    Create a tag in the repository.

    ![08-new-tag](/spring-boot/tutorial/documentation/images/maven/08-new-tag.png "08-new-tag")


1. Install the package in the local maven repository.

    Path: ~/.m2/repository/
    ```bash
    mvn install
    ```

#
## Created by:

1. Luciano Sampaio.
