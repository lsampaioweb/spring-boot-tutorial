Docker Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a YAML file to configure your application’s services. Then, with a single command, you create and start all the services from your configuration.

1. Install Docker, Docker-Compose and other required packages.

    [Docker](docker.md).

1. Create a Dockerfile file.

    Create a `Dockerfile` in the root of your Spring Boot project. This file will define the Docker image for your application.

    See this [Dockerfile](../../../samples/06-container/Dockerfile).

1. Build the Application as a Docker Image.

    ```bash
    docker build --tag=lsampaioweb/container:latest .
    ```

1. Run the container.

    ```bash
    docker run --name my-container -p 8080:8080 lsampaioweb/container:latest

    # -d to run in the background.
    docker run --name my-container -p 8080:8080 -d lsampaioweb/container:1.0

    # -e to pass extra arguments as the port.
    docker run --name my-container -e SERVER_PORT=8081 -p 8080:8081 -d lsampaioweb/container:1.0
    ```

1. Test if everything is working.

    ```bash
    curl http://localhost:8080/api/v1/hello
    ```

1. See the logs.
    ```bash
    docker logs my-container

    # -f to keep watching.
    docker logs -f my-container
    ```

1. Enter in the container.
    ```bash
    docker exec -it my-container sh
    ```

1. Stop the container.
    ```bash
    docker stop my-container
    ```

1. Start the container again.
    ```bash
    docker start my-container
    ```

1. Remove the container.
    ```bash
    docker rm my-container
    ```

1. Create a Docker Compose file.

    Create a `docker-compose.yml` file in the root of your project. This file will define the services (containers) that make up your application.

    See this [docker-compose.yml](../../../samples/06-container/docker-compose.yml).

1. Build the docker image.
    ```bash
    docker-compose build
    ```

1. Run the Application.
    ```bash
    docker-compose up -d

    # -p, --project-name
    docker-compose -p my-app up -d

    # Or
    docker-compose -f my-container-development.yml up -d
    docker-compose -f my-container-production.yml up -d
    ```

#
### Created by:

1. Luciano Sampaio.
