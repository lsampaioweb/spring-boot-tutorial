# Spring Boot with Traefik Reverse Proxy

This sample deploys a Spring Boot app behind `Traefik`, a `reverse proxy`, using `Docker`. It demonstrates dynamic routing with TLS via Traefik labels and a custom Docker image.

## Prerequisites
- Docker and Docker Compose installed.
- Java 21 and Maven for building the app.
- An existing Traefik instance on the `reverse-proxy` network (see step 1 if not set up).

## Steps

1. Create a Bridge Network:

   Connects Traefik and the app for routing.
   ```bash
   docker network create reverse-proxy
   ```

1. Deploy Traefik (if not already running):

   Starts Traefik on a separate machine or locally.
   ```bash
   docker compose up -d
   ```

1. Test Traefik:

   Verify Traefik is running.
   - `https://loadbalancer.lan.homelab`

1. Remove Traefik:

   Stops Traefik.
   ```bash
   docker compose down
   ```

1. Build the Application as a Docker Image:

   Build the application.
   ```bash
   mvn clean package
   ```

   Creates `lsampaioweb/app:1.0` from the Spring Boot app using the `Dockerfile`.
   ```bash
   docker build --tag lsampaioweb/app:1.0 .
   ```

1. Deploy the Application:

   Runs the app with Traefik routing to `app.lan.homelab`.
   ```bash
   docker compose up -d
   ```

1. See the logs.
    ```bash
    docker compose logs app

    # -f to keep watching.
    docker compose logs -f app
    ```

1. Test the Application:

   Hits the app’s endpoint, returning the container’s hostname and IP.
   - https://app.lan.homelab/api/v1/hello

1. Test with Multiple Requests:

   Simulates load to verify Traefik routing and app stability.
   ```bash
   for ((i=1; i<=1000; i++)); do curl -s --max-time 2 https://app.lan.homelab/api/v1/hello; done
   ```

1. Remove the Application:

   Stops and removes the app container.
   ```bash
   docker compose down
   ```

1. Clean Up Unused Containers:
    Removes stopped containers to free resources.
    ```bash
    docker container prune -f
    ```

[Go Back](../../../README.md)

### Created by:
Luciano Sampaio
