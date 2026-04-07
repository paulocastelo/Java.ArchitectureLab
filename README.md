# Java Architecture Lab

Isolated Spring Boot samples demonstrating the most important software engineering patterns in idiomatic Java. Each sample is self-contained and runs independently.

This is the Java counterpart of [CSharp.ArchitectureLab](https://github.com/paulocastelo/CSharp.ArchitectureLab). The same 12 patterns are implemented in both repositories so the architectural decisions and language idioms can be compared directly.

## Samples

| # | Sample | Pattern |
|---|--------|---------|
| [01](samples/01-dependency-injection/) | Dependency Injection | Prototype / Request / Singleton scopes |
| [02](samples/02-repository-pattern/) | Repository Pattern | Interface + Spring Data JPA + `@Profile` swap |
| [03](samples/03-dto-mapping/) | DTOs and Mapping | Manual mapping vs. MapStruct |
| [04](samples/04-validation/) | Validation | Bean Validation + custom `ConstraintValidator` |
| [05](samples/05-error-handling/) | Error Handling | `@RestControllerAdvice` + `ProblemDetail` |
| [06](samples/06-pagination/) | Pagination | Spring `Pageable` + custom `PagedResult<T>` |
| [07](samples/07-unit-testing/) | Unit Testing | JUnit 5 + Mockito, business rule coverage |
| [08](samples/08-auth-basics/) | Auth Basics | Spring Security + JWT filter chain |
| [09](samples/09-logging/) | Logging | SLF4J/Logback structured logging + MDC correlation ID |
| [10](samples/10-cqrs/) | CQRS Basics | Commands, queries, manual Spring bus |
| [11](samples/11-result-pattern/) | Result Pattern | `Result<T>` + sealed classes pattern matching |
| [12](samples/12-outbox-pattern/) | Outbox Pattern | `@Transactional` + `@Scheduled` outbox publisher |

## How to run any sample

```bash
cd samples/01-dependency-injection
mvn spring-boot:run
# Swagger: http://localhost:8001/swagger-ui.html
```

## Stack

- Java 21
- Spring Boot 3.3.x
- H2 in-memory database (where persistence is needed)
- JUnit 5 + Mockito (testing)
- MapStruct, Lombok, Springdoc OpenAPI, jjwt

## See also

[CSharp.ArchitectureLab](https://github.com/paulocastelo/CSharp.ArchitectureLab) — the same 12 patterns implemented in C#/.NET.
