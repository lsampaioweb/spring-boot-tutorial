<!-- filepath: documentation/maven/upgrade.md -->

# Upgrade Spring Boot Samples

This repository keeps each sample application isolated.
That is good for teaching and for real multi-project repositories where each app can evolve at its own pace.

Because of that, upgrading only the Spring Boot parent is not enough.
You must handle parent, properties, dependencies, and plugins explicitly.

## Correct Upgrade Workflow (Exact Commands)

Use the steps below in this order.

### 1) Inspect what can be updated

Run in one project first:

    mvn -f samples/01-pom/pom.xml versions:display-parent-updates versions:display-property-updates versions:display-dependency-updates versions:display-plugin-updates

Run in all projects:

    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} versions:display-parent-updates versions:display-property-updates versions:display-dependency-updates versions:display-plugin-updates \;

### 2) Upgrade Spring Boot parent (exact version)

    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} versions:update-parent -DparentVersion=4.1.0 -DskipResolution=true -DgenerateBackupPoms=false \;

### 3) Upgrade properties that control versions

Examples:

    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} versions:set-property -Dproperty=java.version -DnewVersion=25 -DgenerateBackupPoms=false \;

    find samples -name pom.xml -not -path '*/target/*' -exec grep -l '<spring-cloud.version>' {} \; | xargs -r -I{} mvn -q -f {} versions:set-property -Dproperty=spring-cloud.version -DnewVersion=2025.0.1 -DgenerateBackupPoms=false

### 4) Upgrade dependencies (explicit coordinates)

Use this when a dependency has an explicit version or is not managed by Spring Boot/BOM:

    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} versions:use-dep-version -Dincludes=groupId:artifactId -DdepVersion=1.2.3 -DforceVersion=true -DgenerateBackupPoms=false \;

To apply latest release automatically (less deterministic):

    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} versions:use-latest-releases -Dincludes=groupId:artifactId -DgenerateBackupPoms=false \;

### 5) Upgrade plugins (including maven-surefire-plugin)

Recommended for Spring Boot projects:

1. If a plugin version is managed by Spring Boot parent, do not add `<version>` in your `pom.xml`.
2. Add `<version>` only when you intentionally want to override Spring Boot.

Check which Surefire version Spring Boot is already managing for one project:

    mvn -q -f samples/01-pom/pom.xml help:effective-pom -Doutput=/tmp/sample-effective-pom.xml && grep -n -A3 -m1 '<artifactId>maven-surefire-plugin</artifactId>' /tmp/sample-effective-pom.xml

Find explicit Surefire version overrides that can be removed:

    grep -R --line-number --include='pom.xml' '<artifactId>maven-surefire-plugin</artifactId>\|<version>' samples

If you intentionally want to override Surefire in one project, pin the exact version:

    mvn -f samples/01-pom/pom.xml versions:use-plugin-version -Dincludes=org.apache.maven.plugins:maven-surefire-plugin -DpluginVersion=3.5.14 -DgenerateBackupPoms=false

### 6) Application versions (your project release numbers)

This is independent from Spring Boot and dependencies.

One project:

    mvn -f samples/01-pom/pom.xml versions:set -DnewVersion=2.4.0 -DgenerateBackupPoms=false

All projects:

    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} versions:set -DnewVersion=1.0.0 -DgenerateBackupPoms=false \;

### 7) Validate changes

Review:

    git diff -- samples '**/pom.xml'

Build all:

    find samples -name pom.xml -not -path '*/target/*' -exec mvn -q -f {} clean test \;

## Important Rule for Spring Boot Projects

Avoid pinning versions for dependencies/plugins already managed by Spring Boot parent, unless you intentionally need an override.
Unnecessary overrides increase upgrade risk and maintenance cost.

## Intentional Override Commands

Use these only when Spring Boot is not managing the version you need, or when you explicitly want to override Spring Boot.

Check dependency updates in one project:

    mvn -f samples/01-pom/pom.xml versions:display-dependency-updates

Pin one dependency override in one project:

    mvn -f samples/01-pom/pom.xml versions:use-dep-version -Dincludes=groupId:artifactId -DdepVersion=1.2.3 -DforceVersion=true -DgenerateBackupPoms=false

Check plugin updates in one project:

    mvn -f samples/01-pom/pom.xml versions:display-plugin-updates

Pin one plugin override in one project:

    mvn -f samples/01-pom/pom.xml versions:use-plugin-version -Dincludes=org.apache.maven.plugins:maven-surefire-plugin -DpluginVersion=3.5.14 -DgenerateBackupPoms=false

## Typical Upgrade Sequence

1. Parent (Spring Boot).
2. Ecosystem properties (Spring Cloud, etc.).
3. Explicit dependencies.
4. Explicit plugins (such as surefire if pinned).
5. App release versions.
6. Build and fix breakages.

[Go Back](../../README.md)

#
### Created by:

1. Luciano Sampaio.