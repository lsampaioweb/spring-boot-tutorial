# 22 — RabbitMQ headers

## Run

```bash
cd samples/22-rabbitmq/headers
export RABBITMQ_DEFAULT_USER=admin
export RABBITMQ_DEFAULT_PASS=admin
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Try it

```bash
curl -i -X POST http://localhost:8080/api/v1/messages/headers \
  -H 'Content-Type: application/json' \
  -d '{"customerName":"Ada","product":"Book","quantity":1,"price":9.9,"headerValue":"premium"}'
```
