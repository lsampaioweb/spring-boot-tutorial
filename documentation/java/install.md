# Install Java on Ubuntu

This guide explains how to install Java JDK or JRE on Ubuntu 22.04 for desktop or server environments, targeted at non-beginner Java developers.

1. Update the apt cache to refresh package lists.
    ```bash
    sudo apt update
    ```

1. Search for available Java versions to find the latest OpenJDK.
    ```bash
    sudo apt search openjdk
    ```

1. Install the OpenJDK JDK (e.g., OpenJDK 21) for desktop environments.
    ```bash
    sudo apt install -y openjdk-21-jdk
    ```

1. Install the OpenJDK JRE (e.g., OpenJDK 21) for server environments.
    ```bash
    sudo apt install -y openjdk-21-jre
    ```

1. Verify the Java installation.
    ```bash
    java --version
    ```

1. Open the global profile file to set `JAVA_HOME`.
    ```bash
    sudo nano /etc/profile
    ```

1. Add the `JAVA_HOME` export to the profile file.
    ```bash
    export JAVA_HOME="$(readlink -f `which java` | sed 's:/bin/java::')"
    ```

1. Reload the profile file for the current session. Log out and log in to apply globally.
    ```bash
    source /etc/profile
    ```

[Go Back](../../README.md)

#
### Created by:
1. Luciano Sampaio.
