Maven commands.

## pom.xml conventions

1. `groupId` is always `br.com.lsampaioweb`.
1. `version` keeps the `-SNAPSHOT` suffix during development (standard Maven convention).
   Only drop it when an actual release is cut via `mvn release:prepare` (see below).
1. Add the `<scm>` block from the start, in every module, since every sample belongs to
   this git repository - do not wait until release time to add it.
    ```xml
    <scm>
      <developerConnection>scm:git:https://github.com/lsampaioweb/spring-boot-tutorial.git</developerConnection>
      <tag>HEAD</tag>
    </scm>
    ```
1. Every dependency has a one-line `<!-- purpose -->` comment placed directly above the
   `<dependency>` tag explaining why it's there.
1. Dependencies are grouped and ordered as:
    1. Spring Boot / Spring Cloud / Spring Security starters (framework-provided). Within
       this group, `spring-boot-devtools` goes last since it is a dev-only convenience,
       not a feature starter.
    1. Third-party libraries (non-Spring groupIds), ordered alphabetically by artifactId.
    1. Internal/project-owned dependencies (multi-module projects).
    1. Test-scoped dependencies, always last, regardless of groupId.
1. Do not leave empty `<url />` / `<licenses><license /></licenses>` stubs. If there is
   no real project URL or license decided yet, omit the tags entirely rather than leaving
   them empty.
1. Cumulative topics: once a cross-cutting dependency (Lombok, devtools, etc.) is
   introduced in an earlier sample, later samples reuse it only if that sample's own code
   actually needs it - never add a dependency "just because" a previous sample had it.

1. Compile the application to make sure everything is working.

    ```bash
    mvn compile
    ```

1. Update the version of the library/application in the pom after each new feature.

    ```bash
    mvn release:update-versions
    ```

    Before:

    ![02-version-0-0-1-snapshot](../images/maven/02-version-0-0-1-snapshot.png "02-version-0-0-1-snapshot")

    Enter the new version:

    ![01-choose-version](../images/maven/01-choose-version.png "01-choose-version")

    Maven build success message:

    ![04-version-0-0-2-snapshot-build](../images/maven/04-version-0-0-2-snapshot-build.png "04-version-0-0-2-snapshot-build")

    After:

    ![03-version-0-0-2-snapshot](../images/maven/03-version-0-0-2-snapshot.png "03-version-0-0-2-snapshot")

1. Prepare the next release.

    The `scm` tag should already be present in the pom.xml from creation (see conventions
    above), so no need to add it here.

    Add the `maven-surefire-plugin` plugin.
    ```xml
    <build>
      <plugins>
        ...
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
        </plugin>
              ...
      </plugins>
    </build>
    ```

    Create and publish a new version:

    ```bash
    mvn release:prepare
    ```

    Maven will remove the `SNAPSHOT` from the version.

    ![05-new-release](../images/maven/05-new-release.png "05-new-release")


    Maven will commit and push the new release to the repository.

    ![06-commits](../images/maven/06-commits.png "06-commits")

    Maven will increase the version in the pom.xml.

    ![07-commits-new-version](../images/maven/07-commits-new-version.png "07-commits-new-version")

    Maven will create a tag in the repository.

    ![08-new-tag](../images/maven/08-new-tag.png "08-new-tag")


1. Install the package in the local maven repository, so other applications can use it.

    The path where the application will be installed is: `~/.m2/repository/`
    ```bash
    mvn install
    ```

[Go Back](../../README.md)

#
### Created by:

1. Luciano Sampaio.
