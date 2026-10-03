# Install Java 25 on Ubuntu

This repository requires **Java 25**. Install the JDK before any sample.

## Install

1. Update apt package lists.
    ```bash
    sudo apt update
    ```

1. Install OpenJDK 25.
    ```bash
    sudo apt install -y openjdk-25-jdk
    ```

1. If more than one JDK is installed, select 25.
    ```bash
    sudo update-alternatives --config java
    ```

## Gate (required)

```bash
java --version
```

The output must include `25`. Do not continue the tutorial until it does.

Optional check:

```bash
javac --version
```

## JAVA_HOME (optional)

```bash
nano ~/.bashrc
```

Add:

```bash
export JAVA_HOME="$(readlink -f "$(which java)" | sed 's:/bin/java::')"
```

Reload and verify:

```bash
source ~/.bashrc
echo "$JAVA_HOME"
```

## Notes

- Prefer `openjdk-25-jdk` over `default-jdk`. On many Ubuntu releases, `default-jdk`
  is not Java 25.
- A JRE-only install is not enough for this tutorial (annotation processing and
  compilation need the JDK).

[Go Back](../../README.md)

#
### Created by:
1. Luciano Sampaio.
