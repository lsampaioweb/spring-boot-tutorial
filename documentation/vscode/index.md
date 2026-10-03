# Install VS Code extensions for Java and Spring Boot

Install a small set of extensions for this tutorial. Lombok support comes with
the Extension Pack for Java — you do not need a separate Lombok extension for
normal use.

## Install (recommended)

```bash
code --install-extension vscjava.vscode-java-pack
code --install-extension vmware.vscode-spring-boot
code --install-extension vscjava.vscode-spring-boot-dashboard
code --install-extension vscjava.vscode-spring-initializr
code --install-extension redhat.vscode-xml
code --install-extension redhat.vscode-yaml
```

Optional (containers / remote):

```bash
code --install-extension ms-azuretools.vscode-containers
code --install-extension ms-vscode-remote.remote-containers
```

## Verify

```bash
code --list-extensions
```

## Clear the Java language server cache

When VS Code reports errors on classes that no longer exist after `mvn clean`:

1. Open the Command Palette (`Ctrl+Shift+P`)
1. Run `Java: Clean Java Language Server Workspace`
1. Confirm the reload

## Next

[Create a Spring Boot Project](../setup/project.md) or open `samples/01-pom`.

[Go Back](../../README.md)

#
### Created by:
1. Luciano Sampaio.
