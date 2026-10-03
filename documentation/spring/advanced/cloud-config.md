# Spring Cloud Config

Working sample: `samples/19-cloud-config`. Hands-on runbook:
[`samples/19-cloud-config/README.md`](../../../samples/19-cloud-config/README.md).

This guide shows a Config Server backed by a local Git repository and a client that
imports remote properties through Spring Cloud Config Data.

## Layout

| Path | Role |
| --- | --- |
| `server/` | `@EnableConfigServer` on HTTP `8888` (development) or HTTPS `9443` (production) |
| `client/` | Config client that binds `user.role` into `app.hello` |
| `git-config/` | Local Git backend (`cloud-config-client/{profile}/application.yml`) |

## 1. Initialize the Git backend

```bash
cd samples/19-cloud-config/git-config
./init-repo.sh
```

Example remote property file
(`git-config/cloud-config-client/development/application.yml`):

```yml
user:
  role: "development"
```

## 2. Config Server

Manage Spring Cloud with the BOM, then add the Config Server starter:

```xml
<properties>
  <spring-cloud.version>2025.1.3</spring-cloud.version>
</properties>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>${spring-cloud.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

Point the server at the Git folder and protect client paths with HTTP Basic:

```yml
spring:
  cloud:
    config:
      server:
        git:
          uri: "file://${CONFIG_REPO_PATH:${user.dir}/../git-config}"
          cloneOnStart: true
          search-paths: "{application}/{profile}"
```

Enable the server:

```java
@SpringBootApplication
@EnableConfigServer
public class ServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
```

Development uses plain HTTP on port `8888`. Production enables embedded HTTPS on
`9443` with a PKCS12 keystore (see [HTTPS](../intermediate/https.md)).

## 3. Config Client

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

```yml
spring:
  application:
    name: "cloud-config-client"
  cloud:
    config:
      username: "${CLOUD_CONFIG_CLIENT_USERNAME:cloud-config-client}"
      password: "${CLOUD_CONFIG_CLIENT_PASSWORD}"
  config:
    import: "optional:configserver:http://localhost:8888"

app:
  hello:
    role: "${user.role:local}"
    server-port: "${server.port}"
```

Bind remote values with `@ConfigurationProperties` in the feature package and expose
them through a service + REST controller (`HelloService` / `HelloRestController`).

## 4. Run

See [`samples/19-cloud-config/README.md`](../../../samples/19-cloud-config/README.md)
for environment variables and start commands.

Swagger UI (development): `http://localhost:8080/swagger-ui/index.html`

## Try it (development)

With server and client running (see the runbook), call the client hello endpoint and
expect `user.role` / `app.hello.role` to reflect `development` from the Git backend.

## Previous

[Security](security.md).

## Next
[K6](../tests/k6.md) — `samples/20-k6`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
