XXX

Initializing Docker Swarm
Here are the steps to initialize Docker Swarm and set up your services:

1. Initialize Docker Swarm:

On the manager node (if you are running a single node, this will be your machine), initialize the Swarm:
  docker swarm init

This command will output a docker swarm join command that you can use to add worker nodes to the Swarm. If you are running a single node, you can skip adding more nodes.

docker network create --driver=overlay reverse-proxy

docker stack deploy -c docker-compose.yml traefik -d
docker stack deploy -c docker-compose.yml app -d

docker stack rm traefik
docker stack rm app

1. Build the Application as a Docker Image.

    ```bash
    docker build --tag=lsampaioweb/app:1.0 .
    docker build --tag=lsampaioweb/app:1.1 .
    for ((i=1; i<=1000; i++)); do curl -s --max-time 2 https://app.homelab/api/v1/users; done

    docker stack deploy -c docker-compose.yml app -d
    docker service update --image lsampaioweb/app:1.1 app_app
    docker container prune
    ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
