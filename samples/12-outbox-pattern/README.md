# 12 — Outbox Pattern

## Objective
Show how to persist a domain change and an integration event atomically, then process the outbox asynchronously.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8012/swagger-ui.html`

## What to observe
- `POST /api/products` creates a product and writes an outbox message in the same transaction
- `GET /api/outbox` lets you inspect pending and processed messages
- every 5 seconds the scheduled publisher marks pending messages as processed

## When to use
Use the outbox pattern when a local database update and a future message publication must stay consistent.

## When not to use
Avoid the extra complexity if your app has no integration events or no risk of dual-write inconsistency.

## Comparison with C#
This sample solves the same dual-write problem as the C# version. Spring uses `@Transactional` plus `@Scheduled`, while ASP.NET Core commonly uses EF Core transactions and a hosted `BackgroundService`.
