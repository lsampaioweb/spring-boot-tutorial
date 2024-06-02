Enabling HTTPS in a Spring Boot application ensures that the data transmitted between the client and server is encrypted, enhancing the security of your application. This guide will walk you through configuring HTTPS using separate certificate and private key files.

1. Prepare the Certificate and Key files.

    Make sure you have your certificate (your-certificate.crt) and private key (your-private-key.key) files ready. Place the files in the `resource` folder.

1. Add the config in the `application.yml` file.

    ```yml
    # My custom environment variables.
    KEY_STORE_PASSWORD: "${KEY_STORE_PASSWORD_MY_HTTPS_APP}"

    spring:
      application:
        name: "https"

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
