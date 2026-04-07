# 10 — CQRS Basics

## Objective
Show a lightweight CQRS implementation in Spring without bringing in a heavy framework.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8010/swagger-ui.html`

## What to observe
The controller depends only on `CommandBus` and `QueryBus`, not on a traditional service. Commands and queries are handled by separate Spring-managed handlers.

This is intentionally more didactic than simply depending on a MediatR-like library, because it makes the mechanics of CQRS visible instead of hiding them behind a framework abstraction.

## When to use
Use CQRS when reads and writes have different concerns, models, or scaling needs.

## When not to use
Avoid CQRS in simple CRUD apps where splitting commands and queries adds ceremony without real payoff.

## Comparison with C#
This sample plays the same role as the MediatR-based sample in the C# lab. Here the buses are implemented manually with Spring DI, which makes the Java version even more didactic because it shows explicitly what MediatR abstracts away under the hood in the C# counterpart.
