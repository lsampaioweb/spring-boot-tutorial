# Thymeleaf

Render HTML with server-side templates, form binding, and fragment AJAX.

Working samples under [`samples/13-thymeleaf`](../../../samples/13-thymeleaf)
(catalog: [`README.md`](../../../samples/13-thymeleaf/README.md)):

| Module | Runbook | Focus |
| --- | --- | --- |
| `crud-pages` | [`crud-pages/README.md`](../../../samples/13-thymeleaf/crud-pages/README.md) | Pages + ops form |
| `form-validation` | [`form-validation/README.md`](../../../samples/13-thymeleaf/form-validation/README.md) | Field errors |
| `ajax-interactions` | [`ajax-interactions/README.md`](../../../samples/13-thymeleaf/ajax-interactions/README.md) | Fragment / `fetch` |

Run **one module at a time** — each uses port **8080** in development.

## Before you start

- Previous: [HTTP Client](../intermediate/http-client.md) — `samples/12-http-client`
- Java 25, Maven 3.9+, a browser
- Time: ~30 minutes (all three modules)

## Why this exists

Not every UI is a SPA. Thymeleaf keeps HTML on the server with Spring MVC form
binding and the same i18n keys as REST samples. The three modules grow from
static pages → validated forms → HTML fragments over AJAX.

## What you will see

- HTML pages at `/`, `/message`, `/collection`, `/ops`, `/tasks`
- Form validation error text in the page (not only JSON)
- AJAX create returning a table-row fragment; delete returning **204**

## Run

### 1) crud-pages

```bash
cd samples/13-thymeleaf/crud-pages
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

### 2) form-validation

```bash
cd samples/13-thymeleaf/form-validation
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

### 3) ajax-interactions

```bash
cd samples/13-thymeleaf/ajax-interactions
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Try it

**crud-pages** — open in a browser:

- `http://localhost:8080/`
- `http://localhost:8080/message`
- `http://localhost:8080/collection`
- `http://localhost:8080/ops`

Expected: `200` HTML for each GET; posting the ops form returns the message view.

**form-validation** — `http://localhost:8080/tasks`

Submit empty fields. Expected: page re-rendered with errors such as
“Title is required.”

**ajax-interactions** — `http://localhost:8080/tasks`

Or with curl:

```bash
curl -i -X POST http://localhost:8080/tasks \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'title=TestTask'
```

Expected: `HTTP/1.1 200` and an HTML `<tr>` fragment (not a full page).

```bash
curl -i -X DELETE http://localhost:8080/tasks/1
```

Expected: `HTTP/1.1 204`.

## How the sample is shaped

| Module | Controllers / assets |
| --- | --- |
| `crud-pages` | `HomePageController` (`/`, `/message`, `/collection`), `OpsPageController` (`/ops`) |
| `form-validation` | `TaskPageController` + `@Valid` / `BindingResult`, `templates/task/form.html` |
| `ajax-interactions` | `TaskPageController` fragment return, `static/js/tasks.js`, `th:fragment="task-row"` |

## Tests

From each module folder:

```bash
mvn test
```

Context load (form-validation also runs i18n consistency).

## Stop

`Ctrl+C` before starting the next module.

## Next

[HTTPS](../intermediate/https.md) — `samples/14-https`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
