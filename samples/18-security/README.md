# 18 — Security

HTTP Basic authentication with public, authenticated, and admin endpoints.

## Before you start

- Previous: [17-container](../17-container/README.md)
- Topic: [documentation/spring/advanced/security.md](../../documentation/spring/advanced/security.md)

## Run

```bash
cd samples/18-security
export SECURITY_USER_USERNAME=user
export SECURITY_USER_PASSWORD=change-me
export SECURITY_ADMIN_USERNAME=admin
export SECURITY_ADMIN_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

API port: `8080`. Actuator management port: `8081`.

## Try it

```bash
curl -i http://localhost:8080/api/v1/security/public
```

Expected: `HTTP/1.1 200`.

```bash
curl -i -u user:change-me http://localhost:8080/api/v1/security/profile
```

Expected: `HTTP/1.1 200`.

```bash
curl -i -u user:change-me http://localhost:8080/api/v1/security/admin
```

Expected: `HTTP/1.1 403`.

```bash
curl -i -u admin:change-me http://localhost:8080/api/v1/security/admin
```

Expected: `HTTP/1.1 200`.

## Tests

```bash
mvn test
```

## Next

[19-cloud-config](../19-cloud-config/README.md)
