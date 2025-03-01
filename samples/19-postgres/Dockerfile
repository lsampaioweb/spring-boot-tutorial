# Build the image.
FROM lsampaioweb/alpine-springboot:jre-21-1.0 AS image

# Copy the *.jar file from maven to the image.
COPY ./target/*.jar app.jar

# Command to run the Spring Boot application.
ENTRYPOINT ["java", "-jar", "app.jar"]
