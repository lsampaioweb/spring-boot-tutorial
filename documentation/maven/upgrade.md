<!-- filepath: documentation/maven/upgrade.md -->

# Upgrade Spring Boot Samples

This repository keeps each sample application isolated. That is good for teaching, but it also means each sample has its own `pom.xml` and its own Spring Boot parent version.

Because of that, there is no single Maven command that magically upgrades the whole tutorial for you.

## Recommended Process

1. List what is outdated first.

    Check parent, dependencies, and plugins in one representative sample:

    ```bash
    mvn -f samples/01-pom/pom.xml versions:display-parent-updates versions:display-dependency-updates versions:display-plugin-updates
    ```

1. Update Spring Boot parent versions.

    Pin the target Spring Boot version exactly across all sample projects:

    ```bash
    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} versions:update-parent -DparentVersion=3.5.14 -DskipResolution=true -DgenerateBackupPoms=false \;
    ```

    Why this flag matters: without `-DskipResolution=true`, `parentVersion` is treated as a lower bound for resolution, and Maven may move to a newer parent than the one you typed.

1. Update Java version property when needed.

    Keep Java baseline fixed across all sample projects:

    ```bash
    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} versions:set-property -Dproperty=java.version -DnewVersion=25 -DgenerateBackupPoms=false \;
    ```

1. Review what changed before compiling.

    Inspect only POM changes before moving to build and tests:

    ```bash
    git diff -- samples '**/pom.xml'
    ```

1. Compile and test sample projects.

    Start with simpler samples first, then move to integration-heavy ones:

    ```bash
    mvn -f samples/01-pom/pom.xml clean test
    ```

    Run all sample tests when ready:

    ```bash
    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} clean test \;
    ```

1. Fix breakages sample by sample.

    Typical issues after a Spring Boot upgrade are:

    - dependency version incompatibilities;
    - configuration property changes;
    - test changes caused by framework behavior updates;
    - Lombok annotation processing not working — see [Lombok documentation](../spring/basic/lombok.md).

1. Commit upgrade work in slices.

    Prefer one commit for the mass version bump and separate commits for behavioral fixes. That makes regressions easier to trace.

[Go Back](../../README.md)

#
### Created by:

1. Luciano Sampaio.