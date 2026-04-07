# 01 — Dependency Injection

## Objective
Demonstrate how Spring's IoC container manages bean lifetimes and why constructor injection is the preferred approach.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8001/swagger-ui.html`

## What to observe
Call `GET /api/lifetime` and compare the returned instance IDs:
- `prototype1` and `prototype2` are different
- `singleton1` and `singleton2` are the same
- `request1` and `request2` stay the same within the same HTTP request

## When to use
- **Singleton** for stateless services
- **Prototype** when you need a fresh instance per injection/use
- **Request scope** for request-bound state in web apps

## When not to use
Avoid request/prototype scopes unless the lifetime difference is necessary; singleton is simpler and cheaper.

## Comparison with C#
Spring `prototype` is the closest equivalent to `.NET Transient`, while `@RequestScope` matches `.NET Scoped`. Spring uses annotations and proxies, whereas ASP.NET Core uses explicit DI lifetime registration in `Program.cs`.
