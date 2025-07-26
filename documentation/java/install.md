# Installing Java JDK or JRE on Ubuntu 22.04/24.04

This guide explains how to install Java on Ubuntu Desktop or Server.

**Recommendation:** For development, always install the JDK (Java Development Kit), not just the JRE (Java Runtime Environment).

1. Update the apt cache.
    ```bash
    sudo apt update
    ```

1. (Optional) Upgrade installed packages.
    ```bash
    sudo apt upgrade
    ```

1. Search for all available OpenJDK versions.
    ```bash
    sudo apt-cache search openjdk
    ```

1. Install the recommended JDK for your Ubuntu version (future-proof, recommended for most users).
    ```bash
    sudo apt install -y default-jdk
    ```

1. (Alternative) Install a specific JDK version (e.g., OpenJDK 21).
    ```bash
    sudo apt install -y openjdk-21-jdk
    ```

1. (Optional) For server environments where only running Java apps is needed, install the JRE.
    ```bash
    sudo apt install -y default-jre
    ```

1. Verify the Java installation.
    ```bash
    java --version
    ```

1. (Optional) List all installed Java versions.
    ```bash
    update-java-alternatives --list
    ```

1. (Optional) Switch between installed Java versions.
    ```bash
    sudo update-alternatives --config java
    ```

1. (Optional) Remove an old Java version (replace `<package-name>` with the actual package).
    ```bash
    sudo apt remove --purge <package-name>
    ```

1. Set the JAVA_HOME environment variable.

    **Best practice:** For all users, add to `/etc/environment`. For a single user, add to `~/.profile` or `~/.bashrc`.

    1. Open `/etc/environment` with sudo.
        ```bash
        sudo nano /etc/environment
        ```

    1. Add this line (adjust the path if needed, use `readlink -f $(which java)` to find the path):
        ```bash
        JAVA_HOME="/usr/lib/jvm/java-1.21.0-openjdk-amd64"
        ```
        Or, for dynamic detection:
        ```bash
        JAVA_HOME="$(dirname $(dirname $(readlink -f $(which java))))"
        ```

    1. Log out and log in again, or reload the environment file.
        ```bash
        source /etc/environment
        ```

1. Verify JAVA_HOME is set correctly.
    ```bash
    echo $JAVA_HOME
    ```

**Troubleshooting:**  
If `JAVA_HOME` is not set, double-check the path and ensure you have reloaded your environment or restarted your session.

**Notes:**
- Use `default-jdk` unless you need a specific Java version.
- For development, always use the JDK, not just the JRE.
- Setting JAVA_HOME in `/etc/environment` is preferred for system-wide use; use `~/.profile` or `~/.bashrc` for per-user configuration.

[Go Back](../../README.md)

#
### Created by:

1. Luciano Sampaio.
