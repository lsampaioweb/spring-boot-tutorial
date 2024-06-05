Centralizing application configuration using Spring Boot Config Server simplifies the management of configuration properties across multiple environments and services. This guide will walk you through setting up a Config Server and configuring a Spring Boot application to retrieve its configuration from the server.

1. Prepare the Certificate for your application.

    Follow the instructions in [Create, Verify and Import OpenSSL Certificates](https://github.com/lsampaioweb/openssl-certificates) to create a certificate for this application. After creating the certificate, copy the `.p12` file to the `resources/ssl` directory in your Spring Boot project.

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

#
### Created by:

1. Luciano Sampaio.
