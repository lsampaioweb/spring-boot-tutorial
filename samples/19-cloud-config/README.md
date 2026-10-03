# 19 — Spring Cloud Config

Config Server plus client sample. The server reads a local Git repository under `git-config/` and serves profile-specific YAML. The client imports that configuration over HTTP (development) or HTTPS (production).

## Layout

| Path | Role |
| --- | --- |
| `server/` | Config Server on port `8888` (development) or `9443` HTTPS (production) |
| `client/` | Config client REST API on port `8080` |
| `git-config/` | Local Git backend with `cloud-config-client/{profile}/application.yml` |

## Prerequisites

1. Java 25 and Maven.
1. Initialize the Git backend once:

```bash
cd samples/19-cloud-config/git-config
./init-repo.sh
```

1. For the production profile only: create a PKCS12 keystore at
   `server/src/main/resources/ssl/my-cert.p12` (see
   [HTTPS docs](../../documentation/spring/intermediate/https.md)) and set
   `KEY_STORE_PASSWORD_CLOUD_CONFIG_SERVER`.

## Configuration

| Variable | Used by | Purpose |
| --- | --- | --- |
| `CONFIG_SERVER_PASSWORD` | server | HTTP Basic password for the `CONFIG_ADMIN` user |
| `CLOUD_CONFIG_CLIENT_PASSWORD` | server + client | Shared password for Config Server access |
| `CLOUD_CONFIG_API_PASSWORD` | client | HTTP Basic password for the client REST API |
| `CONFIG_REPO_PATH` | server | Optional override for the `git-config` directory |
| `KEY_STORE_PASSWORD_CLOUD_CONFIG_SERVER` | server (production) | PKCS12 keystore password |

Optional username overrides: `CONFIG_SERVER_USERNAME` (default `config-server`),
`CLOUD_CONFIG_CLIENT_USERNAME` (default `cloud-config-client`),
`CLOUD_CONFIG_API_USERNAME` (default `cloud-config-api`).

## Run

Terminal 1 — server (development, HTTP):

```bash
cd samples/19-cloud-config/server
export CONFIG_SERVER_PASSWORD=change-me
export CLOUD_CONFIG_CLIENT_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Terminal 2 — client:

```bash
cd samples/19-cloud-config/client
export CLOUD_CONFIG_CLIENT_PASSWORD=change-me
export CLOUD_CONFIG_API_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Access

- Client API: `http://localhost:8080/api/v1/hellos` (HTTP Basic)
- Client health: `http://localhost:8080/actuator/health`
- Swagger UI (development): `http://localhost:8080/swagger-ui/index.html`
- Config Server health: `http://localhost:8888/actuator/health`
- Remote property for the development profile: `user.role=development`

Example:

```bash
curl -u cloud-config-api:change-me http://localhost:8080/api/v1/hellos
```

## Tests

```bash
cd samples/19-cloud-config/git-config && ./init-repo.sh
cd ../server && mvn test
cd ../client && mvn test
```

## API summary

| Method | Path | Auth | Description |
| --- | --- | --- | --- |
| `GET` | `/api/v1/hellos` | `CLOUD_CONFIG_API` | Returns a message built from Config Server properties |
