# 07 — Unit Testing

## Objective
Demonstrate JUnit 5 + Mockito for business-rule testing with the same stock-movement rules used in the C# counterpart.

## How to run
```bash
mvn test
```
To start the sample API: `mvn spring-boot:run`

Swagger: `http://localhost:8007/swagger-ui.html`

## What to observe
The test suite covers six scenarios: entry, exit, insufficient balance, exact balance, invalid quantity, and unknown product.

## When to use
Use isolated unit tests when business rules should be verified quickly without infrastructure dependencies.

## When not to use
Do not rely only on unit tests for API behavior; integration tests are still useful for real HTTP/database flows.

## Comparison with C#
This sample mirrors the C# version that uses xUnit + Moq. The Java equivalent relies on JUnit 5 with `@ExtendWith(MockitoExtension.class)` and `when(...).thenReturn(...)` expectations.
