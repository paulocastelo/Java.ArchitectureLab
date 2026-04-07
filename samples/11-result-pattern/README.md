# 11 — Result Pattern

## Objective
Demonstrate two Java approaches for modeling expected failures without throwing exceptions everywhere: a generic `Result<T>` class and sealed success/failure types.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8011/swagger-ui.html`

## What to observe
- `POST /api/products/class` maps a classic `Result<T>` to HTTP responses
- `POST /api/products/sealed` uses Java 21 sealed types plus `switch` pattern matching

## When to use
Use a result pattern when failures are expected business outcomes and you want explicit, testable branches instead of exceptions.

## When not to use
Avoid forcing `Result` everywhere; truly exceptional situations are still better handled with exceptions.

## Comparison with C#
This sample mirrors the C# `Result<T>` example, but also shows how Java 21 sealed interfaces get closer to the expressive switch-based style that feels more natural in modern C#.
