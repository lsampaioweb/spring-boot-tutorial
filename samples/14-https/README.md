# 14 — HTTPS

Serves a small status API over TLS (PKCS12 keystore) on port `9443`.

## Before you start

- Previous: [13-thymeleaf](../13-thymeleaf/README.md)
- Topic: [documentation/spring/intermediate/https.md](../../documentation/spring/intermediate/https.md)
- Generate a local keystore (once):

```bash
cd samples/14-https
./generate-keystore.sh
export KEY_STORE_PASSWORD_MY_HTTPS_APP=change-me
```

## Run

```bash
cd samples/14-https
export KEY_STORE_PASSWORD_MY_HTTPS_APP=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `9443` (HTTPS).

## Try it

```bash
curl -k -i https://localhost:9443/api/v1/https/status
```

Expected: `HTTP/1.1 200` and a JSON status payload. `-k` skips trust of the self-signed cert.

## Tests

```bash
mvn test
```

## Next

[15-events](../15-events/README.md)
