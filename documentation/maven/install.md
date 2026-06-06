# Install Maven on Ubuntu

This guide explains how to install Maven on Ubuntu for desktop or server environments, targeted at non-beginner Java developers.

1. Update the package cache to refresh available packages.
    ```bash
    sudo apt update
    ```

1. Install Maven (e.g., version 3.9.x or later).
    ```bash
    sudo apt install -y maven
    ```

1. Edit the `~/.bashrc` file to set `MAVEN_HOME`.
    ```bash
    nano ~/.bashrc
    ```

1. Add the `MAVEN_HOME` export to the profile file.
    ```bash
    export MAVEN_HOME="$(readlink -f `which mvn` | sed 's:/bin/mvn::')"
    ```

1. Reload the `~/.bashrc` file for the current session.
    ```bash
    source ~/.bashrc
    ```

1. Verify the `MAVEN_HOME` environment variable.
    ```bash
    echo $MAVEN_HOME
    ```

1. Verify the Maven installation.
    ```bash
    mvn --version
    ```

[Go Back](../../README.md)

#
### Created by:
1. Luciano Sampaio.
