# 22 — RabbitMQ topic

## Run

```bash
cd samples/22-rabbitmq/topic
export RABBITMQ_DEFAULT_USER=admin
export RABBITMQ_DEFAULT_PASS=admin
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Try it

Include a `routingKey` that matches the sample bindings (see topic docs / YAML):

```bash
curl -i -X POST http://localhost:8080/api/v1/messages/topic \
  -H 'Content-Type: application/json' \
  -d '{"customerName":"Ada","product":"Book","quantity":1,"price":9.9,"routingKey":"dev.order.created"}'
```
