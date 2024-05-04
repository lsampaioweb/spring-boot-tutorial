Steps to install Java JDK or JRE on Ubuntu 22.04 (desktop or server).

1. Update the apt cache.
```bash
sudo apt update
```

2. Search for all available Java versions.
```bash
sudo apt-cache search openjdk
```

3. On your desktop, install the latest OpenJDK JDK. (For example, OpenJDK 21)
```bash
sudo apt install -y openjdk-21-jdk
```

4. On the server, install the latest JRE.
```bash
sudo apt install -y openjdk-21-jre
```

5. Verify the installation.
```bash
java --version
```

6. Set the JAVA_HOME variable globally.

    6.1. Open the profile file. It will be available for all users.

    ```bash
    sudo nano /etc/profile
    ```

    6.2. Add these lines at the end of the file.

    ```bash
    export JAVA_HOME="$(readlink -f `which java` | sed 's:/bin/java::')"
    ````

    6.3. Reload the profile file.

    The command will reload the file, but only for this terminal session. In order to make it available to all users and terminal sessions, it is necessary to logout and login again.

    ```bash
    source /etc/profile
    ```

#
### Created by:

1. Luciano Sampaio.
