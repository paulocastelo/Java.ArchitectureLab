# Java Architecture Lab

Hands-on **Java 21 / Spring Boot 3.3.x** lab with **12 standalone Spring Boot Web API samples** that demonstrate core backend architecture and software engineering patterns. Each sample is isolated, uses Swagger, and runs on its own fixed port (`8001` to `8012`).

## What this repository covers

- Dependency Injection and bean scopes
- Repository abstraction and persistence boundaries
- DTOs, mapping, and API contracts
- Validation, error handling, and pagination
- Unit testing with JUnit 5 + Mockito
- JWT authentication basics with Spring Security
- Structured logging and correlation IDs with MDC
- CQRS, Result Pattern, and Outbox Pattern

## Samples overview

| # | Sample | Port | Focus |
|---|--------|------|-------|
| [01](samples/01-dependency-injection/) | Dependency Injection | `8001` | Prototype, Request, and Singleton scopes |
| [02](samples/02-repository-pattern/) | Repository Pattern | `8002` | Interface-based persistence abstraction |
| [03](samples/03-dto-mapping/) | DTOs and Mapping | `8003` | Manual mapping vs. MapStruct |
| [04](samples/04-validation/) | Validation | `8004` | Bean Validation + custom constraints |
| [05](samples/05-error-handling/) | Error Handling | `8005` | `@RestControllerAdvice` + `ProblemDetail` |
| [06](samples/06-pagination/) | Pagination | `8006` | Spring `Pageable` + custom `PagedResult<T>` |
| [07](samples/07-unit-testing/) | Unit Testing | `8007` | Business rule tests with JUnit 5 + Mockito |
| [08](samples/08-auth-basics/) | Auth Basics | `8008` | JWT issuing, validation, and protected routes |
| [09](samples/09-logging/) | Logging | `8009` | JSON logs + correlation ID with MDC |
| [10](samples/10-cqrs/) | CQRS Basics | `8010` | Manual `CommandBus` / `QueryBus` implementation |
| [11](samples/11-result-pattern/) | Result Pattern | `8011` | `Result<T>` and sealed success/failure types |
| [12](samples/12-outbox-pattern/) | Outbox Pattern | `8012` | Transactional outbox + scheduled publisher |

## Prerequisites

- `Java 21`
- `Maven 3.9+`
- Optional: VS Code, IntelliJ IDEA, or Spring Tools

## Quick start

### 1. Build and test a sample

```bash
cd samples/07-unit-testing
mvn test
```

### 2. Run any sample

```bash
cd samples/08-auth-basics
mvn spring-boot:run
# Swagger: http://localhost:8008/swagger-ui.html
```

## Repository structure

```text
samples/
  01-dependency-injection/
  02-repository-pattern/
  ...
  12-outbox-pattern/
```

> Each folder contains its own `README.md` with scenario details, endpoints, implementation notes, and comparison with the C# equivalent.

## Comparison with C#

This repository is the Java counterpart of [CSharp.ArchitectureLab](https://github.com/paulocastelo/CSharp.ArchitectureLab). The same 12 patterns are demonstrated in both ecosystems so the architectural decisions and idiomatic differences between **Spring Boot** and **ASP.NET Core** can be compared directly.

A good example is **Sample 10 (CQRS)**: the Java version implements its own `CommandBus` and `QueryBus` instead of relying on a MediatR-like abstraction, which makes the pattern more explicit and more didactic for study purposes.

## Stack

- Java 21
- Spring Boot 3.3.x
- Spring Web MVC
- Spring Data JPA + H2 (when persistence is needed)
- Spring Security + JWT (`jjwt`)
- JUnit 5 + Mockito
- MapStruct
- Springdoc OpenAPI
- Logback + MDC

## Related repository

[CSharp.ArchitectureLab](https://github.com/paulocastelo/CSharp.ArchitectureLab) — the same 12 patterns implemented in C#/.NET.
