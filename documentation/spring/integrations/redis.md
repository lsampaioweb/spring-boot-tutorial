# Spring Boot + Redis

Working samples: `samples/23-redis`. Infrastructure: `samples/infrastructure/redis`.

The compose runbook (start, verify, inspect, stop) lives next to the files: [`samples/infrastructure/redis/README.md`](../../../samples/infrastructure/redis/README.md). This page is the Spring Boot side.

Redis is an in-memory data structure store. The sample is split into three sub-projects:

| Sub-project | Path | Demonstrates |
|-------------|------|--------------|
| `datastore` | `samples/23-redis/datastore` | Redis as the primary data store using `RedisTemplate` and Hash operations |
| `cache-layer` | `samples/23-redis/cache-layer` | Redis as a cache in front of a primary database using `@Cacheable` / `@CacheEvict` |
| `pubsub-events` | `samples/23-redis/pubsub-events` | Pub/Sub messaging using Redis channels and `MessageListenerAdapter` |

Commands below start from the **tutorial repo root**. Replace `docker` with `podman` if that is what you use.

## Prerequisites
1. Docker Compose, or Podman with Podman Compose
1. Java 25
1. Maven 3.9+

## 1. Start Redis

Follow [`samples/infrastructure/redis/README.md`](../../../samples/infrastructure/redis/README.md). Short version:

```bash
cd samples/infrastructure/redis
docker compose up -d
docker exec tutorial-redis redis-cli ping
```

Expect `PONG`. Host: `localhost`, port: `6379`, no password.

## 2. Connection settings

| Parameter | Default | Environment variable |
|-----------|---------|----------------------|
| Host | `localhost` | `REDIS_HOST` |
| Port | `6379` | `REDIS_PORT` |

```yaml
spring:
  data:
    redis:
      host: "${REDIS_HOST:localhost}"
      port: "${REDIS_PORT:6379}"
```

## 3. Run a sample

```bash
cd samples/23-redis/datastore
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Same command from `cache-layer` or `pubsub-events`. Swagger UI (development): `http://localhost:8080/swagger-ui/index.html`

## 4. Redis as a datastore

The datastore sample uses a single Redis Hash named `products`:

| Redis command | `HashOperations` method | Description |
|--------------|------------------------|-------------|
| `HSET products {id} {json}` | `hashOperations.put(key, field, value)` | Save or update |
| `HGET products {id}` | `hashOperations.get(key, field)` | Fetch by ID |
| `HGETALL products` | `hashOperations.entries(key)` | Fetch all |
| `HDEL products {id}` | `hashOperations.delete(key, field)` | Delete by ID |

Products are serialized to JSON with Jackson `ObjectMapper`. `RedisTemplate<String, String>` uses `StringRedisSerializer` for keys and values.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

```java
@Bean
public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
  RedisTemplate<String, String> template = new RedisTemplate<>();
  template.setConnectionFactory(connectionFactory);
  template.setKeySerializer(new StringRedisSerializer());
  template.setValueSerializer(new StringRedisSerializer());
  template.setHashKeySerializer(new StringRedisSerializer());
  template.setHashValueSerializer(new StringRedisSerializer());
  return template;
}
```

### API (`datastore`)

Base URL: `http://localhost:8080/api/v1/products`

| Method | Path | Body | Status | Description |
|--------|------|------|--------|-------------|
| `GET` | `/` | — | 200 | List all products |
| `GET` | `/{id}` | — | 200 / 404 | Get product by UUID |
| `POST` | `/` | `ProductRequest` | 201 | Create product; ID is auto-generated |
| `PUT` | `/{id}` | `ProductRequest` | 200 / 404 | Replace product fields |
| `DELETE` | `/{id}` | — | 204 / 404 | Delete product |

```json
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1299.99
}
```

Inspect Redis:

```bash
docker exec tutorial-redis redis-cli HGETALL products
docker exec tutorial-redis redis-cli HGET products <uuid>
docker exec tutorial-redis redis-cli HLEN products
docker exec tutorial-redis redis-cli DEL products
```

## 5. Redis as a cache

`cache-layer` uses Spring's cache abstraction in front of PostgreSQL:

1. Add `spring-boot-starter-data-redis` and `spring-boot-starter-cache`.
1. `@EnableCaching` on a configuration class (sample: `CacheConfiguration`), with a `RedisCacheManager` and `JacksonJsonRedisSerializer` for the cached response type.
1. Put cache annotations on the **service** implementation:
   - `@Cacheable(cacheNames = "products", key = "#id")` on reads by id
   - `@CachePut(cacheNames = "products", key = "#id")` on update
   - `@CacheEvict(cacheNames = "products", allEntries = true)` on create/delete

Primary persistence stays in the JDBC repository; Redis is cache only.

## 6. Redis Pub/Sub

`pubsub-events` publishes JSON payloads on a Redis channel (`product-events`) with `RedisTemplate.convertAndSend`, and subscribes with `RedisMessageListenerContainer` + `MessageListenerAdapter`.

Context-load tests disable the listener with `redis.pubsub.listener.enabled=false` so they do not require a live subscription.

## 7. Stop Redis

```bash
cd samples/infrastructure/redis
docker compose down
```

Data remains in `samples/infrastructure/redis/volumes` unless you delete that directory.

### Troubleshooting

Infrastructure failures (port 6379, bind-mount permissions, `NOAUTH`) are in [`samples/infrastructure/redis/README.md`](../../../samples/infrastructure/redis/README.md).

If startup fails with `docker-credential-secretservice` missing while using Docker Compose, install Docker credential helpers or remove `credsStore` from `~/.docker/config.json`.

If `podman compose up` fails with `potentially insufficient UIDs or GIDs available in user namespace`, your rootless Podman user is missing subuid/subgid mappings. Ask an administrator to add ranges for your user in `/etc/subuid` and `/etc/subgid`, then run:

```bash
podman system migrate
```

Preflight check:

```bash
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```

If either command returns no line, ask an administrator to add unique ranges, for example:

```bash
usermod --add-subuids 100000-165535 --add-subgids 100000-165535 <username>
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
