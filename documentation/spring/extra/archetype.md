# Spring Boot Maven archetype (maintainer)

Optional workflow to generate a reusable archetype from a baseline project.
Learners should start from the samples in this repository instead.

```bash
curl -o project.zip https://start.spring.io/starter.zip \
    -d dependencies=devtools,lombok,actuator,web,jdbc \
    -d type=maven-project -d language=java -d bootVersion=4.1.1 \
    -d name=spring-boot-base -d groupId=com.learning -d artifactId=spring-boot-base \
    -d javaVersion=25
unzip project.zip -d spring-boot-base

cd spring-boot-base
mvn clean install
mvn archetype:create-from-project

cd target/generated-sources/archetype
mvn clean install

mvn archetype:generate \
  -DarchetypeGroupId=com.learning \
  -DarchetypeArtifactId=spring-boot-base-archetype \
  -DarchetypeVersion=1.0.0-SNAPSHOT \
  -DgroupId=com.learning \
  -DartifactId=my-new-project \
  -Dpackage=com.learning.myapp \
  -Dversion=1.0-SNAPSHOT
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
