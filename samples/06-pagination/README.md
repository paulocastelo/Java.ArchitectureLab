# 06 — Pagination

## Objective
Demonstrate Spring Data pagination with `Pageable`, `Page<T>`, and a custom `PagedResult<T>` API contract.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8006/swagger-ui.html`

## What to observe
Call `GET /api/products?page=0&size=10` and inspect `items`, `totalCount`, `totalPages`, `hasNextPage`, and `hasPreviousPage`.

## When to use
Use a wrapper like `PagedResult<T>` when you want a consistent paged API response instead of exposing Spring's native `Page` shape directly.

## When not to use
Avoid pagination wrappers if the endpoint always returns very small datasets and paging adds unnecessary complexity.

## Comparison with C#
Like the C# sample, this version wraps pagination metadata in a dedicated response object. The main difference is that Spring uses zero-based `page=0`, while the .NET sample uses one-based page numbering.
