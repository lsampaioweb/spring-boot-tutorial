# HTTPS

Working sample: [`samples/14-https`](../../../samples/14-https). Runbook:
[`samples/14-https/README.md`](../../../samples/14-https/README.md).

Serve a small status API over TLS so browsers and clients use HTTPS locally.

## Before you start

- Previous: [Thymeleaf](../basic/thymeleaf.md)
- Java 25, Maven 3.9+, `keytool` (from the JDK)

## Why this exists

HTTP sends traffic in clear text. HTTPS encrypts the channel with TLS. This sample
shows Spring Boot SSL settings with a PKCS12 keystore — without requiring a
homelab CA or desktop secret manager.

## What you will see

- App listens on `https://localhost:9443`
- `GET /api/v1/https/status` returns JSON (use `curl -k` for the self-signed cert)

## Run

```bash
cd samples/14-https
./generate-keystore.sh
export KEY_STORE_PASSWORD_MY_HTTPS_APP=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

The script writes `src/main/resources/ssl/my-cert.p12` (gitignored).

## Try it

```bash
curl -k -i https://localhost:9443/api/v1/https/status
curl -k -H "Accept-Language: pt-BR" https://localhost:9443/api/v1/https/status
```

Expected: `HTTP/1.1 200` and a localized status payload.

## How the sample is shaped

`application.yml` maps the env password into Spring SSL settings:

```yml
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

## Tests

```bash
cd samples/14-https && mvn test
```

## Stop

`Ctrl+C`. Delete `src/main/resources/ssl/my-cert.p12` if you want a fresh cert later.

## Optional: homelab / real CA

If you already use a private CA (for example an openssl-certificates workflow) or
store keystore passwords with `secret-tool`, you can replace the generated `.p12`
and point `KEY_STORE_PASSWORD_MY_HTTPS_APP` at that password. That path is
optional — the local `generate-keystore.sh` script is enough for this lesson.

## Next

[Events](events.md) — `samples/15-events`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
