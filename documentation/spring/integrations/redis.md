# Redis Infrastructure

This project currently provides Redis infrastructure in `samples/infrastructure/redis`.

## Prerequisites
1. Podman with Podman Compose

## 1. Start Redis

```bash
cd samples/infrastructure/redis
podman compose up -d
```

Check health:

```bash
podman compose ps
```

## 2. Validate Redis Manually

```bash
podman exec tutorial-redis redis-cli ping
```

Expected result:

```text
PONG
```

## 3. Connection Settings for Spring Boot Apps

Use these values in your sample apps:

1. Host: `localhost`
1. Port: `6379`

Example properties:

```yaml
spring:
  data:
    redis:
      host: "localhost"
      port: 6379
```

## 4. Stop Redis

```bash
cd samples/infrastructure/redis
podman compose down
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
