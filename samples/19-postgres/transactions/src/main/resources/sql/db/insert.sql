INSERT INTO accounts (id, owner_name, balance) VALUES (1, 'Alice', 1000.00)
ON CONFLICT (id) DO NOTHING;

INSERT INTO accounts (id, owner_name, balance) VALUES (2, 'Bob', 500.00)
ON CONFLICT (id) DO NOTHING;
