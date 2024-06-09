FROM maven:3.9-eclipse-temurin-21-alpine AS build

ARG MAVEN_FOLDER=/opt/mvn

RUN mkdir -p $MAVEN_FOLDER

# Set the working directory.
WORKDIR $MAVEN_FOLDER

COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn -f ${MAVEN_FOLDER}/pom.xml clean package

FROM eclipse-temurin:21-jre-alpine as image

LABEL authors="Luciano Sampaio"

ARG MAVEN_TARGET_FOLDER=/opt/mvn/target
ARG APP_FOLDER=/opt/app

# Create a non root user.
ARG USERNAME=webapp
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN mkdir -p $APP_FOLDER

# Set the working directory.
WORKDIR $APP_FOLDER

# Copy the *.jar file from maven to the image.
COPY --from=build ${MAVEN_TARGET_FOLDER}/*.jar app.jar

# Create the group.
RUN addgroup --system --gid ${USER_GID} ${USERNAME}

# Create the user.
RUN adduser --system --disabled-password --home /home/${USERNAME} --uid ${USER_UID} --ingroup ${USERNAME} ${USERNAME}

# Change the ownership from root to webapp.
RUN chown -R $USERNAME:$USERNAME $APP_FOLDER

USER $USERNAME

EXPOSE 8080

ENTRYPOINT java -jar app.jar
