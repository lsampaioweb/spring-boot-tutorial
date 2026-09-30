-- DBA-owned DDL. Apply with the PostgreSQL client. The app user has DML only; do not run from Spring.

CREATE TABLE IF NOT EXISTS products (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(1000),
  price NUMERIC(19, 2) NOT NULL CHECK (price >= 0)
);
