# 20 — k6

Performance-test scripts. Not a Spring Boot module — point them at a running sample API.

## Before you start

- Install k6 (see [documentation/spring/tests/k6.md](../../documentation/spring/tests/k6.md))
- Start a target app, usually [08-restapi](../08-restapi/README.md) on port `8080`
- Auth scripts need [18-security](../18-security/README.md) with `SECURITY_USER_*` set

## Layout notes

- `01-lifecycle.js` is a commented skeleton (skipped intentionally)
- Prefer `03-simple-get.js` as the first real run
- App-shaped scripts also live under some samples (for example `16-virtual-threads/src/test/k6`)

## Try it

```bash
# from repo root, with 08-restapi running
k6 run samples/20-k6/03-simple-get.js
```

Expected: k6 summary with checks passing against `http://localhost:8080`.

## Next

[21-postgres](../21-postgres/crud/README.md)
