## Spring Boot Async (`@Async`)

Working sample: `samples/27-async/basics`.

This guide shows how to run work off the request thread with Spring `@Async`, track job state, and let clients poll for completion. Application-event *when* to listen asynchronously stays in [events.md](events.md); virtual threads stay in [virtual-threads.md](../advanced/virtual-threads.md).

### Where to look

| Class | Role |
| --- | --- |
| `AsyncBasicsApplication` | `@EnableAsync` |
| `job/AsyncJobWorker` | `@Async` method returning `CompletableFuture` |
| `job/AsyncJobServiceImpl` | Accepts work, hooks `whenComplete`, maps rejections |
| `job/AsyncJobStore` | In-memory lifecycle (queued → running → succeeded/failed) |
| `job/AsyncJobRestController` | `202 Accepted` + `Location`, then GET status |

### Rules that match the sample

1. Add `@EnableAsync` on the application class when any `@Async` method exists.
1. Put `@Async` on a separate Spring bean (the worker). Calling `this` on the same class does not run asynchronously.
1. Return a `CompletableFuture` (or other `CompletionStage`) when the caller must observe completion.
1. For HTTP job APIs: accept quickly (`202` + `Location`), do not block the request thread on the full job, and surface `TaskRejectedException` as a feature error.
1. Do not put `@Async` on REST controller methods.
1. Do not add a custom `TaskExecutor` unless the product needs a named executor beyond Boot's default (virtual-threads contract).

### Run

```bash
cd samples/27-async/basics
export ASYNC_USER_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Swagger UI (development): `http://localhost:8080/swagger-ui/index.html`

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
