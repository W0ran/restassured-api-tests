# RestAssured API Test Automation Framework

End-to-end API test automation suite for the public [restful-api.dev](https://restful-api.dev) REST API, built with **Java**, **RestAssured**, and **TestNG**, following a Service Layer + POJO architecture.

This project was built as a portfolio piece to demonstrate API test automation engineering skills: request/response modeling, JSON Schema validation, and CI-friendly configuration — as a companion to a UI automation project using the same stack (Java/TestNG/Allure).

---

## Tech Stack

| Tool | Purpose |
|---|---|
| **Java 17** | Core language |
| **RestAssured** | HTTP client and assertion DSL for API testing |
| **TestNG** | Test runner, assertions, suite configuration |
| **Maven** | Build tool & dependency management |
| **Jackson** | JSON ↔ POJO serialization/deserialization |
| **JSON Schema Validator** | Structural validation of API responses |
| **Allure** | Test reporting (with automatic request/response attachment via `allure-rest-assured`) |

---

## What's covered

**17 tests** across the full CRUD surface of `/objects`:

- **GET** — list all, get by ID, get by multiple IDs, 404 on invalid ID, JSON Schema validation, nested `data` field deserialization
- **POST** — create with data, create without data, custom attribute shapes, immediate retrievability after creation
- **PUT / PATCH** — full replace vs. partial update semantics, confirms PUT discards old fields that PATCH preserves
- **DELETE** — successful deletion, object unavailable after deletion, repeated deletion, deletion of non-existent object

---

## Architecture highlights

- **Service Layer pattern** (`ObjectsApiService`) — all HTTP calls are centralized in one class; tests never call RestAssured directly. This is the API-testing equivalent of the Page Object Model used in UI automation.
- **Dynamic POJO for flexible schemas** (`ObjectData`) — the API's `data` field has no fixed shape, so it's modeled with Jackson's `@JsonAnySetter`/`@JsonAnyGetter` instead of hardcoded fields, preserving arbitrary attributes without data loss.
- **JSON Schema validation** — response structure is validated against schema files (`src/test/resources/schemas/`), not just spot-checked field by field.
- **Allure + RestAssured integration** — the `AllureRestAssured` filter automatically attaches full request/response payloads to every test step in the report.

---

## ⚠️ Known limitation: API rate limit

`restful-api.dev`'s public (unauthenticated) endpoints are capped at **50 requests/day per IP**. This is a constraint of the free public API, not of the test suite itself.

If you run the full suite multiple times within the same day, you may see `405` responses across all tests instead of expected status codes — this is the rate limit being hit, not a framework bug. The limit resets every 24 hours.

For CI, this means the suite should be run sparingly (e.g., on `push` to `main`, not on every commit) to stay within quota.

---

## Project structure

```
restassured-api-tests/
├── pom.xml
├── testng.xml
└── src/
    └── test/
        ├── java/
        │   └── com/restfulapidev/
        │       ├── models/
        │       │   ├── ObjectData.java
        │       │   ├── ObjectRequest.java
        │       │   └── ObjectResponse.java
        │       ├── services/
        │       │   └── ObjectsApiService.java
        │       ├── tests/
        │       │   ├── BaseApiTest.java
        │       │   ├── GetObjectsTests.java
        │       │   ├── CreateObjectTests.java
        │       │   ├── UpdateObjectTests.java
        │       │   └── DeleteObjectTests.java
        │       └── utils/
        │           └── TestDataFactory.java
        └── resources/
            └── schemas/
                ├── object-schema.json
                └── objects-list-schema.json
```

---

## Running the tests

### Prerequisites
- Java 17+
- Maven 3.8+

### Run all tests
```bash
mvn clean test
```

### Generate Allure report
```bash
mvn allure:report
mvn allure:serve
```

---

## Author

**Yerkebulan Rakhymbay** — QA Engineer & Systems Analyst

- LinkedIn: [linkedin.com/in/yerkebulan-rakhymbay-29444336a](https://www.linkedin.com/in/yerkebulan-rakhymbay-29444336a/)
- GitHub: [github.com/W0ran](https://github.com/W0ran)
- Email: woran.96.kaz.kz@gmail.com