1. Uninstall old versions.

    Before you can install Docker Engine, you need to uninstall any conflicting packages.

    ```bash
    sudo apt remove -y docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc
    ```

1. Add Docker's official GPG key:

    ```bash
    sudo apt update
    sudo apt install ca-certificates curl
    sudo install -m 0755 -d /etc/apt/keyrings
    sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
    sudo chmod a+r /etc/apt/keyrings/docker.asc
    ```

1. Add the repository to Apt sources:

    ```bash
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt update
    ```

1. Install the Docker packages.

    ```bash
    sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose docker-compose-plugin
    ```

1. Add the permissions.

    ```bash
    sudo groupadd docker
    sudo usermod -aG docker $USER
    newgrp docker
    sudo systemctl restart docker
    ```

1. Verify that the Docker Engine installation is successful by running the `hello-world` image.

    * No need to run with sudo.
    ```bash
    docker run hello-world
    ```

#
### Created by:

1. Luciano Sampaio.
