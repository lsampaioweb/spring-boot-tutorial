1. Create a `bridge` Network:
    ```bash
    docker network create reverse-proxy
    ```

1. Deploy Traefik:
    ```bash
    docker-compose.yml up -d
    ```

1. Test Traefik:

    - https://loadbalancer.homelab

1. Remove Traefik:
    ```bash
    docker-compose.yml down
    ```

1. Build the Application as a Docker Image:
    ```bash
    docker build --tag=lsampaioweb/app:1.0 .
    ```

1. Deploy the application:
    ```bash
    docker-compose.yml up -d
    ```

1. Test the application:

    - https://app.homelab/api/v1/users

1. Test the application with multiple requests:

    ```bash
    for ((i=1; i<=1000; i++)); do curl -s --max-time 2 https://app.homelab/api/v1/users; done
    ```

1. Remove the application:
    ```bash
    docker-compose.yml down
    ```

1. Clean up unused containers:
    ```bash
    docker container prune
    ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
