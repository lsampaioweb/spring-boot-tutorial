# Topic / sample documentation template

Use this shape for every topic page under `documentation/spring/**` and every
`samples/**/README.md`. Sample READMEs may omit "Build it yourself" when the
topic page already covers recreation.

## Required sections

### Title

`# NN — Short title`

One sentence: what you will be able to do after this lesson.

### Before you start

- Previous topic: link (or "none — start here")
- You need: Java 25 (`java --version`), Maven 3.9+, Docker/Podman if any
- Working sample: `samples/NN-name` (clone and run)
- Time: ~N minutes

### Why this exists

3–6 sentences. When to use it vs the previous sample.

### What you will see

Bullet list of observable outcomes (log line, HTTP status, UI).

### Run

From the sample folder, with profile and env vars. Example:

~~~bash
cd samples/NN-name
export SOME_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
~~~

Infra first, if any: link to `samples/infrastructure/{service}/README.md`.

### Try it

Show `curl` (or UI steps) and **Expected** status/body/log line.

Optional: Swagger URL, second locale (`Accept-Language: pt-BR`).

### How the sample is shaped

Table: class/file → role. Link into the repo; do not paste large classes.

### Build it yourself (optional)

Minimal pom/YAML/Java diffs that match the sample.

### Tests

~~~bash
mvn test
~~~

What the tests prove in one sentence.

### Stop

`Ctrl+C`; `docker compose down` if infra was started.

### Troubleshooting

Only failures this sample actually hits.

### Next

Link to the next catalog item.

## Rule

If a motivated new hire cannot finish Run + Try it without opening Java sources,
the page is not done.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
