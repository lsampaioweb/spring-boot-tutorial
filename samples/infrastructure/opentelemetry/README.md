# OpenTelemetry Collector

OpenTelemetry Collector for this tutorial. Apps in `samples/28-tracing` export traces over OTLP; the Collector prints them to **container stdout** (no Jaeger/Tempo UI).

Compose creates the shared network `tutorial-network` on first `up`. You do not need a manual `docker network create` / `podman network create` before starting the Collector.

All commands below assume the tutorial repo root is your current directory, then `cd samples/infrastructure/opentelemetry`. Replace `docker` with `podman` if that is what you use.

## Prerequisites

1. Docker Compose, or Podman with Podman Compose.
1. Host ports `4317` (OTLP gRPC) and `4318` (OTLP HTTP) free.

Rootless Podman, once per machine:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## Start

```bash
cd samples/infrastructure/opentelemetry
docker compose up -d
```

Check status:

```bash
docker compose ps
```

## Verify

Follow a request from `samples/28-tracing`, then watch Collector stdout:

```bash
docker compose logs -f otel-collector
```

You should see exported spans (service names `tracing-caller` and `tracing-callee`) after hitting the caller demo endpoint.

## Stop

```bash
docker compose down
```

## Troubleshoot

| Symptom | What to check |
| --- | --- |
| Apps log OTLP export errors | Collector is up (`docker compose ps`); apps point at `http://localhost:4318/v1/traces` |
| No spans in logs | Sampling is `1.0` in the sample YAML; hit `GET http://localhost:8080/api/v1/traces/demo` with both apps running |
| Port already in use | Stop another Collector/agent bound to `4317`/`4318` |
