# 03 — DTOs and Mapping

## Objective
Show why API responses should expose DTOs instead of domain entities, and compare manual mapping with MapStruct.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8003/swagger-ui.html`

## What to observe
- `GET /api/products/mapstruct` uses a compile-time generated MapStruct mapper
- `GET /api/products/manual` uses a manual static mapper
- `internalCost` and `internalNotes` never leak in the response DTO

## When to use
Use DTOs whenever your API contract should be stable, safe, and independent from domain internals.

## When not to use
Avoid excessive mapping layers when the app is tiny and the domain object is already the API contract.

## Comparison with C#
This sample mirrors the C# version that compares manual mapping with AutoMapper. In Java, MapStruct is compile-time and explicit, which usually makes it easier to reason about than runtime reflection-based mapping.
