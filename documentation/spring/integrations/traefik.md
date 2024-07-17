1. Initialize `Docker Swarm`:
    ```bash
    docker swarm init
    ```

1. Create an `Overlay` Network:
    ```bash
    docker network create --driver=overlay reverse-proxy
    ```

1. Deploy Traefik:
    ```bash
    docker stack deploy -c docker-compose.yml traefik -d
    ```

1. Build the Application as a Docker Image:
    ```bash
    docker build --tag=lsampaioweb/app:1.0 .
    ```

1. Deploy the Application:
    ```bash
    docker stack deploy -c docker-compose.yml app -d
    ```

1. Build a newer version of the Application:
    ```bash
    docker build --tag=lsampaioweb/app:1.1 .
    ```

1. Test the application with multiple requests:

    Update the stack by running the deploy command again, then fire this test to see the versions changing:
    ```bash
    for ((i=1; i<=1000; i++)); do curl -s --max-time 2 https://app.homelab/api/v1/users; done
    ```

1. Remove the Applications:
    ```bash
    docker stack rm traefik
    docker stack rm app
    ```

1. Clean up unused containers:
    ```bash
    docker container prune
    ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
