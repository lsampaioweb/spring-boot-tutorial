# 23 — Redis cache-layer

Redis cache in front of PostgreSQL products. Needs **both** Postgres and Redis.

## Infrastructure

```bash
cd samples/infrastructure/postgres
cp .env.example .env
set -a && source .env && set +a
docker compose up -d

cd ../redis
docker compose up -d
```

From the **repo root**, optionally apply the DBA schema (Spring also runs classpath
`sql/schema.sql` when `spring.sql.init.mode=always`):

```bash
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/23-redis/cache-layer/src/main/resources/sql/db/schema.sql
```

## Run

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME="$POSTGRES_DB"
export DB_USER="$POSTGRES_USER"
export DB_PASSWORD="$POSTGRES_PASSWORD"

cd samples/23-redis/cache-layer
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

```bash
curl -i http://localhost:8080/api/v1/products
```

Expected: `HTTP/1.1 200`. Repeat the same GET and watch logs for cache hits.

## Stop

Stop the app with `Ctrl+C`. Infra: `docker compose down` in each infrastructure folder.
