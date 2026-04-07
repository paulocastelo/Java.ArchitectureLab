# 02 — Repository Pattern

## Objective
Show how application code can depend on an abstraction while Spring swaps the concrete persistence implementation with `@Profile`.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8002/swagger-ui.html`

## What to observe
- `GET /api/products` reads through `ProductService` using the `ProductRepository` interface
- `POST /api/products` writes using the active implementation
- change `spring.profiles.active` from `memory` to `jpa` to swap the repository implementation

## When to use
Use this pattern when you want to isolate application rules from persistence details and keep infrastructure replaceable.

## When not to use
Avoid adding an extra repository abstraction when your app is very small and `JpaRepository` is already enough.

## Comparison with C#
This sample mirrors the C# lab where `IProductRepository` can be backed by EF Core or in-memory storage. Spring uses `@Profile` and Spring Data JPA, while .NET commonly swaps implementations directly in the DI container.
