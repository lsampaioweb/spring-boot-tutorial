FROM lsampaioweb/maven:3.9.6 AS build

COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=${APP_FOLDER}/.m2 mvn -f pom.xml clean package

FROM lsampaioweb/springboot:1.0 AS image

LABEL authors="Luciano Sampaio"

# Copy the *.jar file from maven to the image.
COPY --from=build ${MAVEN_TARGET_FOLDER}/*.jar app.jar

# Command to run the Spring Boot application.
ENTRYPOINT ["java", "-jar", "app.jar"]
