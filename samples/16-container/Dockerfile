# Use the Java web image to run the application.
FROM docker.io/lsampaioweb/java-web:25-alpine.3.23-2026.05

# Copy the *.jar file to the image, setting ownership to the app user and group.
COPY --chown=${APP_USER_NAME}:${APP_GROUP_NAME} ./target/*.jar app.jar

# JVM options for the Spring Boot application (can be overridden at runtime).
ENV JAVA_TOOL_OPTIONS=""

# Expose ports for HTTP and HTTPS traffic (optional, for web apps only).
EXPOSE 8080 9443

# Command to run the Spring Boot application with JVM options.
ENTRYPOINT ["java", "-jar", "app.jar"]
