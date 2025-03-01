curl -o project.zip https://start.spring.io/starter.zip \
    -d dependencies=devtools,lombok,actuator,web,jdbc \
    -d type=maven-project -d language=java -d bootVersion=3.4.3 \
    -d name=spring-boot-base -d groupId=com.learning -d artifactId=spring-boot-base \
    unzip project.zip -d spring-boot-base

mvn clean install

mvn archetype:create-from-project -DpropertyFile=archetype.properties

cd spring-boot-base/target/generated-sources/archetype

mvn clean install

mvn archetype:generate \
  -DarchetypeGroupId=com.learning \
  -DarchetypeArtifactId=spring-boot-base \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.learning \
  -DartifactId=my-new-project \
  -Dpackage=com.learning.myapp \
  -Dversion=1.0-SNAPSHOT
