CREATE TABLE IF NOT EXISTS accounts (
  id BIGSERIAL PRIMARY KEY,
  owner_name VARCHAR(255) NOT NULL,
  balance NUMERIC(19, 2) NOT NULL CHECK (balance >= 0)
);

INSERT INTO accounts (id, owner_name, balance) VALUES (1, 'Alice', 1000.00)
ON CONFLICT (id) DO NOTHING;

INSERT INTO accounts (id, owner_name, balance) VALUES (2, 'Bob', 500.00)
ON CONFLICT (id) DO NOTHING;
