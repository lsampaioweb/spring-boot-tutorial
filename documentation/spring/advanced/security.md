## Spring Security

Working sample: `samples/18-security`

This lesson adds HTTP Basic authentication and role-based authorization to a small REST API. Earlier samples stay open on purpose. Do not add `spring-boot-starter-security` until this step.

### What the sample demonstrates

1. Deny-by-default: unknown paths return `403`.
1. Stateless HTTP Basic (no form login, no CSRF token for this API).
1. An in-memory `UserDetailsService` with `USER` and `ADMIN` roles.
1. Credentials loaded from environment variables, then encoded with BCrypt.
1. Method security: `@PreAuthorize("hasRole('ADMIN')")` on the admin use case.
1. Actuator on a separate port (`8081`) so operational endpoints are not mixed with the API.

### 1. Add dependencies

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Tests also use `spring-security-test`.

### 2. Supply credentials from the environment

`application.yml`:

```yml
app:
  security:
    credentials:
      user:
        username: ${SECURITY_USER_USERNAME}
        password: ${SECURITY_USER_PASSWORD}
      admin:
        username: ${SECURITY_ADMIN_USERNAME}
        password: ${SECURITY_ADMIN_PASSWORD}
```

`SecurityProperties` binds that prefix and rejects blank values.

Run:

```bash
cd samples/18-security
SECURITY_USER_USERNAME=user \
SECURITY_USER_PASSWORD=change-me \
SECURITY_ADMIN_USERNAME=admin \
SECURITY_ADMIN_PASSWORD=change-me \
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

### 3. Configure the filter chain

The sample:

1. Disables CSRF because HTTP Basic is stateless for this API.
1. Uses `SessionCreationPolicy.STATELESS`.
1. Enables HTTP Basic.
1. Allows `/api/v1/security/public` without authentication.
1. Requires authentication for `/api/v1/security/profile`.
1. Requires `ADMIN` for `/api/v1/security/admin`.
1. Opens Swagger only when the development profile enables `springdoc`.
1. Denies everything else.

`@EnableMethodSecurity` plus `@PreAuthorize` on `SecurityServiceImpl.getAdminMessage()` is a second guard for the admin path.

### 4. Call the endpoints

```bash
curl -i http://localhost:8080/api/v1/security/public

curl -i -u user:change-me http://localhost:8080/api/v1/security/profile

curl -i -u user:change-me http://localhost:8080/api/v1/security/admin

curl -i -u admin:change-me http://localhost:8080/api/v1/security/admin
```

Expected results:

1. Public: `200` with a localized message.
1. Profile as `user`: `200` and the username in the body.
1. Admin as `user`: `403`.
1. Admin as `admin`: `200`.

Pass `Accept-Language: pt-BR` to resolve messages from `i18n/messages_pt_BR.properties`.

### 5. Load tests

`samples/20-k6/11-basic-authentication.js` and `samples/20-k6/12-setup-auth.js` target this API. Start this sample first, then run those scripts with the same `SECURITY_USER_*` environment variables.

Swagger UI (development profile): `http://localhost:8080/swagger-ui/index.html`

Runbook: [`samples/18-security/README.md`](../../../samples/18-security/README.md).

## Previous

[Container](../extra/container.md).

## Next
[Cloud Config](cloud-config.md) — `samples/19-cloud-config`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
