-- DBA-owned DDL. Apply with the PostgreSQL client. The app user has DML only; do not run from Spring.

CREATE TABLE IF NOT EXISTS accounts (
  id BIGSERIAL PRIMARY KEY,
  owner_name VARCHAR(255) NOT NULL,
  balance NUMERIC(19, 2) NOT NULL CHECK (balance >= 0)
);
