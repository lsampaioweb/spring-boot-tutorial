Enabling HTTPS in a Spring Boot application ensures that the data transmitted between the client and server is encrypted, enhancing the security of your application.

1. Prepare the Certificate for your application.

    Follow the instructions in [Create, Verify and Import OpenSSL Certificates](https://github.com/lsampaioweb/openssl-certificates) to create a certificate for this application. After creating the certificate, copy the `.p12` file to the `resources/ssl` directory in your Spring Boot project.

1. Save the password of the Private Key in the Secret Manager.

    ```bash
    secret-tool store --label="ssl.jump-server-01.homelab" password "ssl.jump-server-01.homelab"
    ```

1. Confirm the password was correctly saved.
    ```bash
    secret-tool lookup password "ssl.jump-server-01.homelab"
    ```

1. Add the Environmente variable in the `~/.bashrc` file.

    To avoid conflicts with other applications, name the environment variable uniquely, e.g: `KEY_STORE_PASSWORD_MY_HTTPS_APP`.
    ```bash
    nano ~/.bashrc
    export KEY_STORE_PASSWORD_MY_HTTPS_APP=$(secret-tool lookup password "ssl.jump-server-01.homelab")
    ```

1. Reload your bash to load the new environment variable.
    ```bash
    source ~/.bashrc
    ```

1. Add the configuration in the `application.yml` file.

    This configuration sets up your Spring Boot application to use the `.p12` certificate for HTTPS on port `9443`, with `TLS 1.3` and `HTTP/2` enabled.
    ```yml
    # My custom environment variables.
    KEY_STORE_PASSWORD: "${KEY_STORE_PASSWORD_MY_HTTPS_APP}"

    server:
      port: 9443
      ssl:
        enabled: true
        enabled-protocols: "TLSv1.3"
        key-store-type: "PKCS12"
        key-store: "classpath:ssl/my-cert.p12"
        key-store-password: ${KEY_STORE_PASSWORD}
      http2:
        enabled: true
    ```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
