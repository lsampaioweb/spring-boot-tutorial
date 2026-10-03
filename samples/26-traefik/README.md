# 26 — Traefik

Spring Boot app routed through Traefik with a Host rule.

## Before you start

- Infra: [samples/infrastructure/traefik/README.md](../infrastructure/traefik/README.md)
- Topic: [documentation/spring/integrations/traefik.md](../../documentation/spring/integrations/traefik.md)

```bash
cd samples/infrastructure/traefik
cp .env.example .env   # if present
docker compose up -d
```

## Run the app

```bash
cd samples/26-traefik
export SECURITY_ACTUATOR_USERNAME=actuator
export SECURITY_ACTUATOR_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Direct (no Traefik): port `8080`.

For Compose packaging of this sample, follow the topic page (build image, join
`tutorial-network`, Traefik labels).

## Try it

Direct:

```bash
curl -i http://localhost:8080/api/v1/users/hello
```

Via Traefik (Host must match labels; default often `app.lan.home`):

```bash
curl -i -H 'Host: app.lan.home' http://localhost/api/v1/users/hello
```

Expected: `HTTP/1.1 200`.

## Tests

```bash
mvn test
```

## Next

[27-async/basics](../27-async/basics/README.md)
