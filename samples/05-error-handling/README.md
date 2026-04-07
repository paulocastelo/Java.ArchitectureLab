# 05 — Error Handling

## Objective
Show how Spring maps exceptions to consistent HTTP responses using `@RestControllerAdvice` and `ProblemDetail`.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8005/swagger-ui.html`

## What to observe
Call the `/api/demo/*` endpoints to see how each exception maps to a different status code and payload.

## When to use
Use centralized exception handling to keep controllers clean and API error contracts consistent.

## When not to use
Do not use exceptions for every expected validation branch; simple request validation or `Result` patterns can be clearer.

## Comparison with C#
This is the Spring equivalent of ASP.NET Core exception middleware. Spring uses `@RestControllerAdvice` and `@ExceptionHandler`, while the C# sample uses pipeline middleware to shape the same HTTP responses.
