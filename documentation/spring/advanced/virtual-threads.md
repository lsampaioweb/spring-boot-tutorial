## Virtual Threads

Working sample: `samples/16-virtual-threads`.

Virtual threads (Project Loom) let a servlet/MVC app keep blocking I/O — JDBC
and `RestClient` — without one platform thread per request. Spring Boot
turns them on with a YAML flag. No extra dependency is required; this tutorial
already uses Java 25.

### Enable virtual threads

Put the flag in shared `application.yml`, not only in one profile:

```yml
spring:
  threads:
    virtual:
      enabled: true
```

Do not add a custom `TaskExecutor` or `Executors.newVirtualThreadPerTaskExecutor()`
bean. Boot already uses virtual threads for request handling when this flag is
on.

Do not lower `server.tomcat.threads.max` as a virtual-thread trick. The sample
keeps that knob commented only so you can experiment.

### What the sample demonstrates

1. `HttpBinRestController` is a thin REST controller at `/api/v1/httpbins`.
1. `GET /api/v1/httpbins/block/{seconds}` delegates to `HttpBinService`.
1. The service makes a **blocking** `RestClient` call to a delay endpoint, then
   returns a `DelayResponse` with the HTTP status and `Thread.currentThread()`.
1. The controller logs that result through an i18n log key.

With virtual threads enabled, the logged thread looks like a virtual thread
(for example `VirtualThread[#…]`). With the flag set to `false`, it is a
platform Tomcat worker.

### Try it

1. Run the sample with the `development` profile (port `8080`).
1. Call `http://localhost:8080/api/v1/httpbins/block/3`.
1. Check the response `thread` field and the application log.
1. Set `spring.threads.virtual.enabled` to `false`, restart, and call the same
   URL again to compare.

Load scripts for this endpoint live under `src/test/k6/` in the sample. The k6
topic covers how to run them.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
