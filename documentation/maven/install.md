Steps to install Maven on Ubuntu 22.04 (desktop or server).

1. Update the apt cache.
```bash
sudo apt update
```

2. Install the latest version.
```bash
sudo apt install -y maven
```

3. Verify the installation.
```bash
mvn --version
```

4. Set the MAVEN_HOME variable globally.

    4.1. Open the profile file. It will be available for all users.

    ```bash
    sudo nano /etc/profile
    ```

    4.2. Add these lines at the end of the file.

    ```bash
    export MAVEN_HOME="$(readlink -f `which mvn` | sed 's:/bin/mvn::')"
    ````

    4.3. Reload the profile file.

    The command will reload the file, but only for this terminal session. In order to make it available to all users and terminal sessions, it is necessary to logout and login again.

    ```bash
    source /etc/profile
    ```

#
## Created by:

1. Luciano Sampaio.
