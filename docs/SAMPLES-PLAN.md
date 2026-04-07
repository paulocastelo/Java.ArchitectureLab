# Samples Plan — Java.ArchitectureLab

This document lists all samples to be implemented in this repository, their purpose, and the concepts each one demonstrates.

Each sample is a self-contained Spring Boot Web API project. They share the same inventory domain (products, categories) to keep the focus on the architectural pattern, not on learning a new domain.

This repository is the Java counterpart of `CSharp.ArchitectureLab`. The same 12 patterns are demonstrated in both, highlighting how idiomatic Java/Spring Boot approaches compare to idiomatic C#/.NET approaches.

---

## Sample List

### 01 — Dependency Injection

**Folder:** `samples/01-dependency-injection`

**Concepts demonstrated:**
- Spring's IoC container and bean lifecycle
- `@Component`, `@Service`, `@Repository` stereotypes
- constructor injection (preferred) vs. field injection (`@Autowired`)
- `@Scope` annotations: singleton (default), prototype, request
- why constructor injection is preferred over field injection in Spring

**What the sample contains:**
- `GreetingService` interface with three implementations annotated with different scopes
- `LifetimeController` injecting them and returning instance IDs
- a README comparing Spring `@Scope` with .NET DI lifetimes (Singleton/Scoped/Transient)

---

### 02 — Repository Pattern

**Folder:** `samples/02-repository-pattern`

**Concepts demonstrated:**
- Spring Data JPA repositories vs. custom repository interfaces
- `JpaRepository<T, ID>` as a built-in implementation
- switching between in-memory and JPA implementations via `@Primary` / `@Profile`
- `@Transactional` annotation for write operations

**What the sample contains:**
- `ProductRepository` interface extending `JpaRepository`
- `InMemoryProductRepository` with `@Profile("test")`
- `JpaProductRepository` with `@Profile("dev")` using H2 (in-memory SQL)
- `ProductService` depending only on the interface
- a README comparing Spring Data with EF Core repository pattern

---

### 03 — DTOs and Mapping

**Folder:** `samples/03-dto-mapping`

**Concepts demonstrated:**
- Java records as immutable DTOs
- MapStruct for compile-time mapping (vs. AutoMapper in .NET)
- manual mapping as an alternative
- why domain entities should not be exposed directly in API responses

**What the sample contains:**
- `Product` JPA entity with internal fields (`internalCost`, `internalNotes`)
- `CreateProductRequest` record
- `ProductResponse` record exposing only safe fields
- `ProductMapper` interface annotated with `@Mapper` (MapStruct)
- manual mapping as an extension method equivalent (static factory)
- a README comparing MapStruct with AutoMapper

---

### 04 — Validation

**Folder:** `samples/04-validation`

**Concepts demonstrated:**
- Bean Validation (`@NotBlank`, `@Size`, `@Email`, `@Min`) on request records
- `@Valid` on controller parameters to trigger validation
- custom `ConstraintValidator` for complex rules
- `@ControllerAdvice` + `MethodArgumentNotValidException` for consistent error responses

**What the sample contains:**
- `CreateProductRequest` validated with Bean Validation annotations
- `RegisterUserRequest` with a custom `@StrongPassword` constraint
- `GlobalExceptionHandler` formatting `MethodArgumentNotValidException` into:
  ```json
  {
    "type": "validation_error",
    "errors": {
      "email": ["must be a well-formed email address"],
      "password": ["Password must be at least 8 characters."]
    }
  }
  ```
- a README comparing Bean Validation with FluentValidation

---

### 05 — Error Handling

**Folder:** `samples/05-error-handling`

**Concepts demonstrated:**
- `@ControllerAdvice` as the Spring equivalent of ASP.NET Core middleware
- `@ExceptionHandler` methods for each exception type
- `ResponseStatusException` vs. custom exception hierarchy
- `ProblemDetail` (RFC 7807) — available in Spring 6+

**What the sample contains:**
- `NotFoundException`, `ConflictException`, `BusinessRuleException` (runtime exceptions)
- `GlobalExceptionHandler` with `@ControllerAdvice` mapping each to HTTP status
- `ProblemDetail` responses:
  ```json
  {
    "type": "not_found",
    "detail": "Product with id 'abc-123' was not found.",
    "traceId": "abc123"
  }
  ```
- `DemoController` with endpoints throwing each exception type
- a README comparing `@ControllerAdvice` with ASP.NET Core middleware approach

---

### 06 — Pagination

**Folder:** `samples/06-pagination`

**Concepts demonstrated:**
- Spring Data's built-in `Pageable` parameter
- `Page<T>` response from Spring Data
- custom `PagedResult<T>` wrapper for a consistent API response
- `@PageableDefault` to set default page size

**What the sample contains:**
- `ProductRepository` extending `JpaRepository` with `findAll(Pageable pageable)`
- `ProductService.findAll(Pageable pageable)` returning `PagedResult<ProductResponse>`
- `PagedResult<T>` record with `items`, `page`, `pageSize`, `totalCount`, `totalPages`, `hasNextPage`, `hasPreviousPage`
- H2 seeded with 100 products on startup
- endpoint: `GET /api/products?page=0&size=10` (Spring is zero-indexed)
- a README noting the Spring zero-index convention vs. .NET one-index

---

### 07 — Unit Testing

**Folder:** `samples/07-unit-testing`

**Concepts demonstrated:**
- JUnit 5 with `@ExtendWith(MockitoExtension.class)`
- Mockito `when(...).thenReturn(...)` and `assertThrows`
- test naming convention: `methodName_scenario_expectedResult`
- `@BeforeEach` setup
- the same business rules tested in the C# counterpart

**What the sample contains:**
- `StockMovementService` with the same balance validation rules as the C# sample
- `StockMovementServiceTest` with 6 test cases (identical logic to `StockMovementServiceTests.cs`)
- a README comparing JUnit 5 + Mockito with xUnit + Moq

---

### 08 — Auth Basics (JWT)

**Folder:** `samples/08-auth-basics`

**Concepts demonstrated:**
- Spring Security filter chain configuration
- `OncePerRequestFilter` as the JWT authentication filter
- `SecurityContextHolder` for storing the authenticated principal
- BCrypt via Spring Security's `PasswordEncoder`
- jjwt library for token generation and validation

**What the sample contains:**
- `AuthController` with register and login endpoints
- `ProfileController` with a `GET /api/profile` endpoint protected by `@PreAuthorize` or `@Secured`
- `JwtTokenProvider` (same as `StockFlow.NoSQL.MobileFirst`)
- `JwtAuthenticationFilter extends OncePerRequestFilter`
- `SecurityConfig` with stateless session management
- Springdoc Swagger configured with Bearer token
- a README comparing Spring Security filter chain with ASP.NET Core middleware pipeline

---

### 09 — Logging

**Folder:** `samples/09-logging`

**Concepts demonstrated:**
- SLF4J + Logback (Spring Boot default) vs. Serilog in .NET
- structured logging with Logback JSON encoder
- `@Slf4j` Lombok annotation vs. explicit `LoggerFactory.getLogger`
- MDC (Mapped Diagnostic Context) for correlation IDs — Spring equivalent of `LogContext.PushProperty`
- Spring's built-in `CommonsRequestLoggingFilter` vs. custom filter

**What the sample contains:**
- `logback-spring.xml` configured for JSON console output and rolling file
- `CorrelationIdFilter extends OncePerRequestFilter` using MDC
- `RequestLoggingFilter` logging method, path, status, and duration
- `ProductService` demonstrating `log.debug`, `log.info`, `log.warn`, `log.error`
- a README comparing SLF4J/MDC with Serilog/LogContext

---

### 10 — CQRS Basics

**Folder:** `samples/10-cqrs`

**Concepts demonstrated:**
- CQRS without a framework vs. with a lightweight mediator
- Spring's `ApplicationEventPublisher` as a simple command bus
- manual command/query handlers wired via Spring DI
- comparison with MediatR (.NET)

**What the sample contains:**
- `CreateProductCommand` record + `CreateProductCommandHandler` (`@Component`)
- `GetProductByIdQuery` record + `GetProductByIdQueryHandler`
- `CommandBus` / `QueryBus` interfaces with a Spring-managed dispatcher
- `ProductsController` depending only on the buses — no service injection
- a README explaining two approaches: manual Spring bus vs. Axon Framework vs. MediatR (.NET)

---

### 11 — Result Pattern

**Folder:** `samples/11-result-pattern`

**Concepts demonstrated:**
- Java does not have discriminated unions natively — different approaches to simulate Result
- generic `Result<T>` class in Java
- comparison with `Optional<T>` and when each is appropriate
- sealed classes (Java 17+) as an alternative

**What the sample contains:**
- `Result<T>` class with `isSuccess()`, `getValue()`, `getError()`, `getErrorCode()`
- `ProductService.create` returning `Result<ProductResponse>` for expected failures
- controller mapping `Result` to HTTP responses
- a second implementation using sealed classes (`Success` / `Failure` subtypes)
- a README comparing Java Result with C# Result, and noting why Java lacks the elegance of C# records here

---

### 12 — Outbox Pattern

**Folder:** `samples/12-outbox-pattern`

**Concepts demonstrated:**
- `@Transactional` ensuring atomic write of domain entity + outbox message
- Spring's `@Scheduled` for the outbox publisher background task
- JPA entity for `OutboxMessage`
- the same conceptual problem as the C# sample, solved with Spring idioms

**What the sample contains:**
- `OutboxMessage` JPA entity with `eventType`, `payload`, `createdAtUtc`, `processedAtUtc`
- `ProductService.create` using `@Transactional` to save product + outbox atomically
- `OutboxPublisher` with `@Scheduled(fixedDelay = 5000)` reading and processing pending messages
- H2 database with a `GET /api/outbox` endpoint to visualize processed and pending messages
- a README comparing Spring `@Transactional` + `@Scheduled` with EF Core transactions + `BackgroundService`

---

## Structure Rule

Every sample must include:

- `README.md` explaining:
  - what problem the pattern solves
  - how to run (`mvn spring-boot:run`)
  - what to observe in the output or Swagger
  - when to use and when not to use this pattern
  - how it compares to the C# equivalent (one paragraph)
- working code that compiles and runs
- no shared state between samples (each is fully independent Maven project)

---

## Technology

- Java 21
- Spring Boot 3.3.x
- H2 (in-memory, where persistence is needed — no database install required)
- Spring Data JPA (persistence samples)
- jjwt 0.12.x (auth sample)
- Springdoc OpenAPI 2.x (Swagger)
- JUnit 5 + Mockito (testing sample)
- MapStruct (DTO mapping sample)
- Lombok (optional — can be used across all samples to reduce boilerplate)
- Maven
