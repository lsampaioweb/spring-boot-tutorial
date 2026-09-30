# Install VS Code Extensions for Java and Spring Boot

This guide explains how to install essential `VS Code` extensions for `Java` and `Spring Boot` development on `Ubuntu`, targeted at non-beginner Java developers.

1. Install extensions via terminal for automation.
    ```bash
    code --install-extension dbaeumer.vscode-eslint
    code --install-extension hashicorp.hcl
    code --install-extension hashicorp.terraform
    code --install-extension ms-azuretools.vscode-containers
    code --install-extension ms-dotnettools.vscode-dotnet-runtime
    code --install-extension ms-mssql.data-workspace-vscode
    code --install-extension ms-mssql.mssql
    code --install-extension ms-mssql.sql-bindings-vscode
    code --install-extension ms-mssql.sql-database-projects-vscode
    code --install-extension ms-python.debugpy
    code --install-extension ms-python.python
    code --install-extension ms-python.vscode-pylance
    code --install-extension ms-python.vscode-python-envs
    code --install-extension ms-vscode-remote.remote-containers
    code --install-extension ms-vscode-remote.remote-ssh
    code --install-extension ms-vscode-remote.remote-ssh-edit
    code --install-extension ms-vscode.remote-explorer
    code --install-extension ms-vscode.vscode-chat-customizations-evaluations
    code --install-extension redhat.ansible
    code --install-extension redhat.java
    code --install-extension redhat.vscode-xml
    code --install-extension redhat.vscode-yaml
    code --install-extension sonarsource.sonarlint-vscode
    code --install-extension tomoki1207.pdf
    code --install-extension vmware.vscode-spring-boot
    code --install-extension vscjava.vscode-gradle
    code --install-extension vscjava.vscode-java-debug
    code --install-extension vscjava.vscode-java-dependency
    code --install-extension vscjava.vscode-java-pack
    code --install-extension vscjava.vscode-java-test
    code --install-extension vscjava.vscode-maven
    code --install-extension vscjava.vscode-spring-boot-dashboard
    code --install-extension vscjava.vscode-spring-initializr
    code --install-extension vscode-icons-team.vscode-icons
    ```

1. List installed extensions.
    ```bash
    code --list-extensions
    ```

1. Clear the Java language server workspace cache.

    **Purpose:** Clears the VS Code Java language server's stale workspace cache (type index, resolved classes, etc.) for this project. Run this when VS Code reports errors on classes that no longer exist, even after `mvn clean`.

    Open the Command Palette (`Ctrl+Shift+P`), run `Java: Clean Java Language Server Workspace`, and confirm the reload when prompted. This clears the cache and triggers a full reindex automatically - no need to manually find or delete the `workspaceStorage` cache folder.

[Go Back](../../README.md)

#
### Created by:
1. Luciano Sampaio.
