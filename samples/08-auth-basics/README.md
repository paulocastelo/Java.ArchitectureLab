# 08 — Auth Basics (JWT)

## Objective
Show stateless authentication in Spring Boot using Spring Security, a JWT filter, and BCrypt password hashing.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8008/swagger-ui.html`

## What to observe
- `POST /api/auth/register` creates a user
- `POST /api/auth/login` returns a JWT bearer token
- `GET /api/profile` requires the token and returns the authenticated user claims

## When to use
Use JWT when you need stateless API authentication across services or SPAs/mobile apps.

## When not to use
Avoid JWT for simple server-rendered apps when regular sessions are enough and easier to rotate/revoke.

## Comparison with C#
This sample follows the same idea as the ASP.NET Core sample and the `StockFlow.NoSQL.MobileFirst` backend. Spring uses a filter chain and `OncePerRequestFilter`, while ASP.NET Core uses authentication middleware registered in the HTTP pipeline.
