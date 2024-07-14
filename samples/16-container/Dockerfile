# Build stage.
FROM lsampaioweb/maven:3.9.6 AS build

# Copy pom.xml and download dependencies.
COPY pom.xml ./pom.xml
RUN --mount=type=cache,target=${HOME}/.m2,uid=${UID},gid=${GID} mvn dependency:go-offline -X

# Copy the source code and build the application.
COPY src ./src
RUN --mount=type=cache,target=${HOME}/.m2,uid=${UID},gid=${GID} mvn clean package -X

# Final stage.
FROM lsampaioweb/springboot:1.0 AS image

# Set metadata.
LABEL authors="Luciano Sampaio"

# Copy the *.jar file from maven to the image.
COPY --from=build ${HOME}/target/*.jar app.jar

# The port that the application will be listening for connections.
ARG SERVER_PORT=8080

# Expose the port the application runs on.
EXPOSE ${SERVER_PORT}

# Command to run the Spring Boot application.
ENTRYPOINT ["java", "-jar", "app.jar"]
