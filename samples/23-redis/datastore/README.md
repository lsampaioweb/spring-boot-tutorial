# 23 — Redis datastore

Product CRUD stored in Redis hashes.

## Infrastructure

```bash
cd samples/infrastructure/redis
docker compose up -d
```

## Run

```bash
cd samples/23-redis/datastore
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

```bash
curl -i http://localhost:8080/api/v1/products
```

Expected: `HTTP/1.1 200` with a product list (empty until you create one).
