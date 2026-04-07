# 09 — Logging

## Objective
Show structured JSON logging with Logback, correlation IDs via MDC, and request timing filters.

## How to run
```bash
mvn spring-boot:run
```
Swagger: `http://localhost:8009/swagger-ui.html`

## What to observe
- every response includes `X-Correlation-Id`
- request logs include method, path, status, and duration
- service logs demonstrate `debug`, `info`, `warn`, and `error`

## When to use
Use correlation IDs and structured logs whenever troubleshooting across requests, services, or async flows matters.

## When not to use
Avoid excessive debug logging in production unless it is necessary and monitored, because it increases noise and cost.

## Comparison with C#
This sample is the Java/Spring counterpart to the C# sample that uses Serilog and `LogContext`. In Spring, MDC carries the correlation ID through the logging context instead of Serilog enrichers.
