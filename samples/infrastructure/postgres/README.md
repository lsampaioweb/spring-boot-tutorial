# PostgreSQL

Relational database for this tutorial (`samples/21-postgres` and Redis `cache-layer`). Host port `5432`.

Credentials come only from `.env` (copy `.env.example` and change them). Compose fails if `POSTGRES_DB`, `POSTGRES_USER`, or `POSTGRES_PASSWORD` are unset. The Spring samples read `DB_NAME`, `DB_USER`, and `DB_PASSWORD` from the environment — they have no fallback passwords in YAML.

PostgreSQL 18 stores files under `/var/lib/postgresql` (not `/var/lib/postgresql/data`). That path is bind-mounted at `./volumes` and survives `compose down`.

The Spring apps do not run DDL. Apply each sample's `sql/db/*.sql` with `psql` (commands below). The application user is treated as DML-only in the lesson; locally `POSTGRES_USER` can still run these scripts.

Compose creates the shared network `tutorial-network` on first `up`. You do not need a manual `docker network create` / `podman network create` before starting PostgreSQL.

Traefik HTTP routing does not apply here. JVM samples use `localhost:5432`. Other containers on `tutorial-network` can use hostname `tutorial-postgres`.

All commands below assume the tutorial repo root is your current directory, then `cd samples/infrastructure/postgres`. Replace `docker` with `podman` if that is what you use.

## Prerequisites

1. Docker Compose, or Podman with Podman Compose.
1. Host port `5432` free.

Rootless Podman, once per machine:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## Start

```bash
cd samples/infrastructure/postgres
cp .env.example .env
# edit .env — do not commit it
set -a && source .env && set +a
docker compose up -d
```

Wait until healthy:

```bash
docker compose ps
docker exec tutorial-postgres pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB" -h 127.0.0.1
```

Expect `accepting connections`.

## Apply sample SQL (first time, or after wiping volumes)

Load `.env` first (`set -a && source .env && set +a`). From the tutorial repo root, `crud` and `batch` share the `users` table:

```bash
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/crud/src/main/resources/sql/db/schema.sql
```

Transactions sample:

```bash
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/transactions/src/main/resources/sql/db/schema.sql
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/transactions/src/main/resources/sql/db/insert.sql
```

List tables:

```bash
docker exec tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c '\dt'
```

## Spring samples

Map the infrastructure `.env` onto the names the apps expect:

```bash
cd samples/infrastructure/postgres
set -a && source .env && set +a
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME="$POSTGRES_DB"
export DB_USER="$POSTGRES_USER"
export DB_PASSWORD="$POSTGRES_PASSWORD"
```

How the apps use the database is in [postgresql.md](../../../documentation/spring/integrations/postgresql.md).

## Stop

```bash
cd samples/infrastructure/postgres
docker compose down
```

Data stays in `./volumes`. Delete that directory if you want a blank database (you must re-apply the SQL files).

`compose down` leaves `tutorial-network` in place if another stack is still using it.

## Troubleshooting

**`POSTGRES_PASSWORD:?set POSTGRES_PASSWORD in .env`.** Copy `.env.example` to `.env` in this folder. Compose interpolates that file; there is no password in `docker-compose.yml`.

**Password authentication failed.** `POSTGRES_*` is applied only on first init. If you changed `.env` after the first `up`, either keep the old password or delete `./volumes` and start again.

**Port 5432 already in use.**

```bash
ss -tlnp | grep ':5432 '
```

**Permission denied on `/var/lib/postgresql`.** Alpine Postgres runs as UID `70`:

```bash
sudo chown -R 70:70 volumes
```

Rootless Podman often works without this because UIDs are mapped to your user.

**Leftover PG 17 (or older) files in `./volumes/data`.** PostgreSQL 18 will not use that path. Wipe `./volumes` and start clean, or dump/restore. Do not point this compose file at a pre-18 data directory.

**`docker-credential-secretservice` missing.** Install Docker credential helpers, or remove `credsStore` from `~/.docker/config.json`.

**Podman: `potentially insufficient UIDs or GIDs available in user namespace`.** Ask an administrator to add subuid/subgid ranges, then `podman system migrate`.

```bash
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```
