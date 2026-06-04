# Install Java on Ubuntu

This guide explains how to install Java JDK or JRE on Ubuntu for desktop or server environments, targeted at non-beginner Java developers.

1. Update the apt cache to refresh package lists.
    ```bash
    sudo apt update
    ```

1. Search for available Java versions to find the latest OpenJDK.
    ```bash
    sudo apt search openjdk
    ```

1. Install the recommended JDK for your Ubuntu version (future-proof, recommended for most users).
    ```bash
    sudo apt install -y default-jdk
    ```

1. (Alternative) Install a specific JDK version (e.g., OpenJDK 25).
    ```bash
    sudo apt install -y openjdk-25-jdk
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

1. Add the `JAVA_HOME` export to the profile file.
    ```bash
    export JAVA_HOME="$(readlink -f `which java` | sed 's:/bin/java::')"
    ```

1. Reload the profile file for the current session. Log out and log in to apply globally.
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
