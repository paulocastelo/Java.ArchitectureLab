# 04 — Validation

## Objective
Demonstrate Bean Validation, custom constraints, and consistent API validation errors in Spring Boot.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8004/swagger-ui.html`

## What to observe
- `POST /api/products` validates basic product fields
- `POST /api/users/register` validates email and a custom strong-password rule
- invalid requests return a normalized `validation_error` payload with field-specific messages

## When to use
Use request validation at the API boundary to reject bad input early and keep controllers predictable.

## When not to use
Do not put every business rule into annotations; complex domain rules should still live in services/domain logic.

## Comparison with C#
This sample is the Spring equivalent of the C# sample that uses Data Annotations plus FluentValidation. In Java, Bean Validation integrates directly with `@Valid`, while custom constraints replace specialized validator classes.
