<!-- filepath: documentation/spring/integrations/redis.md -->

# Redis Integration with Spring Boot

## Overview

Redis is an open-source, in-memory data structure store. It can act as a **database**, **cache**, or **message broker**.

The Redis sample is split into three focused sub-projects:

| Sub-project | Path | Demonstrates |
|-------------|------|--------------|
| `datastore` | `samples/22-redis/datastore` | Redis as the primary data store using `RedisTemplate` and Hash operations |
| `cache-layer` | `samples/22-redis/cache-layer` | Redis as a cache in front of a primary database using `@Cacheable` / `@CacheEvict` |
| `pubsub-events` | `samples/22-redis/pubsub-events` | Pub/Sub messaging using Redis channels and `MessageListenerAdapter` |

## Prerequisites

1. Java 25
1. Maven 3.9+
1. Podman with Podman Compose

## Key Concepts

### Redis Hash Operations

The sample uses a single Redis Hash named `products`:

| Redis command | `HashOperations` method | Description |
|--------------|------------------------|-------------|
| `HSET products {id} {json}` | `hashOperations.put(key, field, value)` | Save or update |
| `HGET products {id}` | `hashOperations.get(key, field)` | Fetch by ID |
| `HGETALL products` | `hashOperations.entries(key)` | Fetch all |
| `HDEL products {id}` | `hashOperations.delete(key, field)` | Delete by ID |

### JSON Serialization

Products are serialized to JSON strings with Jackson `ObjectMapper` before storage and deserialized back on retrieval. The `RedisTemplate<String, String>` is configured to use `StringRedisSerializer` for all keys and values — the repository layer owns the serialization contract.

### Spring Data Redis Setup

Add to `pom.xml`:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

Configure `application.yml`:

```yaml
spring:
  data:
    redis:
      host: "${REDIS_HOST:localhost}"
      port: "${REDIS_PORT:6379}"
```

Configure a `RedisTemplate` bean (see `RedisConfig.java`):

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

## Architecture of `samples/22-redis`

```
com.learning.redis
├── RedisApplication.java          ← @SpringBootApplication entry point
├── OpenApiConfig.java             ← Springdoc OpenAPI bean
├── redis/
│   └── RedisConfig.java           ← RedisTemplate<String, String> bean
├── exception/
│   ├── AppException.java          ← Abstract base for all domain exceptions
│   ├── ErrorResponse.java         ← Error DTO (record)
│   ├── ValidationError.java       ← Per-field validation error DTO (record)
│   └── GlobalExceptionHandler.java← @RestControllerAdvice
├── i18n/
│   └── LogMessages.java           ← i18n helper for log messages
└── product/
    ├── Product.java               ← Domain record (id, name, description, price)
    ├── ProductRequest.java        ← Input DTO for POST / PUT (record)
    ├── ProductNotFoundException.java ← extends AppException
    ├── ProductController.java     ← REST controller: /api/v1/products
    ├── ProductService.java        ← Business logic + UUID generation
    └── ProductRepository.java     ← Redis Hash operations
```

## Infrastructure Setup

### 1. Start Redis

```bash
cd samples/infrastructure/redis
podman compose up -d
```

Verify Redis is healthy:

```bash
podman compose ps
podman exec tutorial-redis redis-cli ping
```

Expected result:

```text
PONG
```

### 2. Connection Settings

| Parameter | Default | Environment variable |
|-----------|---------|----------------------|
| Host | `localhost` | `REDIS_HOST` |
| Port | `6379` | `REDIS_PORT` |

## Running the Sample

Choose the sub-project:

```bash
# Datastore
cd samples/22-redis/datastore
mvn spring-boot:run -Dspring-boot.run.profiles=development

# Cache layer
cd samples/22-redis/cache-layer
mvn spring-boot:run -Dspring-boot.run.profiles=development

# Pub/Sub events
cd samples/22-redis/pubsub-events
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## API Reference

Base URL: `http://localhost:8080/api/v1/products`

| Method | Path | Body | Status | Description |
|--------|------|------|--------|-------------|
| `GET` | `/` | — | 200 | List all products |
| `GET` | `/{id}` | — | 200 / 404 | Get product by UUID |
| `POST` | `/` | `ProductRequest` | 201 | Create product; ID is auto-generated |
| `PUT` | `/{id}` | `ProductRequest` | 200 / 404 | Replace product fields |
| `DELETE` | `/{id}` | — | 204 / 404 | Delete product |

`ProductRequest` body:

```json
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1299.99
}
```

## Inspecting Redis Directly

List all products stored in the Hash:

```bash
podman exec tutorial-redis redis-cli HGETALL products
```

Get a specific product by ID:

```bash
podman exec tutorial-redis redis-cli HGET products <uuid>
```

Count stored products:

```bash
podman exec tutorial-redis redis-cli HLEN products
```

Delete all products (flush the hash):

```bash
podman exec tutorial-redis redis-cli DEL products
```

## Using Redis as a Cache Layer (Alternative Pattern)

If you want Redis as a **cache** in front of an existing database instead of a data store, replace the direct `RedisTemplate` calls with Spring's Cache abstraction:

1. Add `@EnableCaching` to your main application class.
1. Annotate service methods:
   - `@Cacheable("products")` — cache the result on first call; return cached value on subsequent calls
   - `@CachePut(value = "products", key = "#product.id")` — always update the cache after a write
   - `@CacheEvict(value = "products", key = "#id")` — remove the entry from cache on delete

Spring Boot auto-configures the Redis cache store when `spring-boot-starter-data-redis` is on the classpath and `@EnableCaching` is present.

## Stop Infrastructure

```bash
cd samples/infrastructure/redis
podman compose down
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
