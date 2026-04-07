# Especificação Técnica — Java.ArchitectureLab

Este documento é a especificação de implementação para todos os samples do repositório.
Cada sample é um projeto Spring Boot independente dentro da pasta `samples/`.

---

## Convenções gerais

- Cada sample vive em `samples/NN-nome/` com seu próprio `pom.xml`
- Nenhum sample depende de outro
- Onde precisar de banco de dados, usar **H2** (in-memory, sem instalação)
- Cada sample tem um `README.md` com: objetivo, como rodar, o que observar, quando usar, comparação com C#
- Todos devem compilar com `mvn clean package` e rodar com `mvn spring-boot:run`
- Swagger habilitado em todos os samples (`http://localhost:PORT/swagger-ui.html`)
- Porta padrão de cada sample: `8001` + número do sample (8001, 8002, ..., 8012)
- Pacote base: `com.archlab.sampleNN`

---

## `pom.xml` base (herdar em todos os samples)

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.0</version>
</parent>

<properties>
    <java.version>21</java.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.5.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Adicionar dependências específicas por sample conforme especificado abaixo.

---

## 01 — Dependency Injection

**Porta:** 8001
**Dependências extras:** nenhuma

**Estrutura:**
```
src/main/java/com/archlab/sample01/
  services/
    IGreetingService.java      // interface
    TransientGreeting.java     // @Component @Scope("prototype")
    ScopedGreeting.java        // @Component @Scope("request")
    SingletonGreeting.java     // @Component (default)
  controllers/
    LifetimeController.java
Application.java
```

**Implementação:**

```java
public interface IGreetingService {
    String greet();
}

@Component
@Scope("prototype")
public class PrototypeGreetingService implements IGreetingService {
    private final String instanceId = UUID.randomUUID().toString();
    @Override public String greet() { return "Prototype: " + instanceId; }
}
```

`LifetimeController` deve injetar **dois beans** de cada tipo para demonstrar visualmente o comportamento. Para injetar dois do mesmo tipo, usar `@Qualifier` ou `@Autowired` com `List<IGreetingService>` separados por tipo:

```java
@RestController
@RequestMapping("/api/lifetime")
public class LifetimeController {
    private final PrototypeGreetingService prototype1;
    private final PrototypeGreetingService prototype2;
    private final SingletonGreetingService singleton1;
    private final SingletonGreetingService singleton2;
    // ...construtor...

    @GetMapping
    public Map<String, String> show() {
        return Map.of(
            "prototype1", prototype1.greet(),
            "prototype2", prototype2.greet(),   // diferente de prototype1
            "singleton1", singleton1.greet(),
            "singleton2", singleton2.greet()    // igual a singleton1
        );
    }
}
```

**Nota importante:** `@Scope("request")` requer um contexto web ativo. Para este sample, usar `prototype` (equivalente a Transient) e `singleton` (equivalente a Singleton) — anotar no README que `request` scope equivale ao Scoped do .NET.

---

## 02 — Repository Pattern

**Porta:** 8002
**Dependências extras:** `spring-boot-starter-data-jpa`, `com.h2database:h2`

**Estrutura:**
```
src/main/java/com/archlab/sample02/
  domain/
    Product.java             // @Entity
  application/
    ProductRepository.java   // interface
    ProductService.java
    contracts/
      CreateProductRequest.java
      ProductResponse.java
  infrastructure/
    InMemoryProductRepository.java   // @Profile("memory")
    JpaProductRepository.java        // @Profile("jpa") extends JpaRepository
    AppJpaRepository.java            // interface estendendo JpaRepository
  controllers/
    ProductsController.java
Application.java
```

`ProductRepository` interface:
```java
public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(String id);
    Product save(Product product);
}
```

`InMemoryProductRepository`:
```java
@Repository
@Profile("memory")
public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> store = new ConcurrentHashMap<>();
    // implementação...
}
```

`JpaProductRepository`:
```java
@Repository
@Profile("jpa")
public class JpaProductRepository implements ProductRepository {
    private final AppJpaRepository jpaRepository;
    // delega para Spring Data...
}
```

`application.properties`:
```properties
spring.profiles.active=memory
# trocar para: spring.profiles.active=jpa
```

**Nota no README:** comparar `@Profile` do Spring com o comentário de swap no `Program.cs` do .NET.

---

## 03 — DTOs and Mapping

**Porta:** 8003
**Dependências extras:** `org.mapstruct:mapstruct:1.5.5.Final`, `org.mapstruct:mapstruct-processor:1.5.5.Final`

**Estrutura:**
```
src/main/java/com/archlab/sample03/
  domain/
    Product.java              // internalCost, internalNotes presentes
  application/
    contracts/
      CreateProductRequest.java   // record
      ProductResponse.java        // record — sem internalCost
    mappings/
      ProductMapper.java          // @Mapper(componentModel = "spring")
    extensions/
      ProductMapper manual/       // método estático toResponse(Product)
    ProductService.java
  controllers/
    ProductsController.java
Application.java
```

`ProductMapper` (MapStruct):
```java
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);
    // MapStruct ignora campos não presentes em ProductResponse automaticamente
}
```

Dois endpoints:
```
GET /api/products/mapstruct   // usa ProductMapper (MapStruct)
GET /api/products/manual      // usa método estático
```

**Plugin Maven obrigatório no `pom.xml`:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.5.Final</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

---

## 04 — Validation

**Porta:** 8004
**Dependências extras:** `spring-boot-starter-validation`

**Estrutura:**
```
src/main/java/com/archlab/sample04/
  contracts/
    CreateProductRequest.java    // Bean Validation annotations
    RegisterUserRequest.java     // @StrongPassword custom constraint
  validation/
    StrongPassword.java          // @interface (annotation)
    StrongPasswordValidator.java // implements ConstraintValidator
  exception/
    GlobalExceptionHandler.java  // @ControllerAdvice
  controllers/
    ProductsController.java
    UsersController.java
Application.java
```

`CreateProductRequest`:
```java
public record CreateProductRequest(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 50) String sku,
    @DecimalMin("0.01") BigDecimal unitPrice
) {}
```

`StrongPassword` annotation:
```java
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "Password must be at least 8 characters with uppercase and digit.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

`GlobalExceptionHandler`:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.groupingBy(
                FieldError::getField,
                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
            ));
        return Map.of("type", "validation_error", "errors", errors);
    }
}
```

---

## 05 — Error Handling

**Porta:** 8005
**Dependências extras:** nenhuma

**Estrutura:**
```
src/main/java/com/archlab/sample05/
  exceptions/
    NotFoundException.java
    ConflictException.java
    BusinessRuleException.java
  exception/
    GlobalExceptionHandler.java   // @RestControllerAdvice
  controllers/
    DemoController.java
Application.java
```

Exceções:
```java
public class NotFoundException extends RuntimeException {
    public NotFoundException(String detail) { super(detail); }
}
// idem para Conflict e BusinessRule
```

`GlobalExceptionHandler`:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setProperty("type", "not_found");
        problem.setProperty("traceId", getTraceId(request));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflict(ConflictException ex, HttpServletRequest request) { ... }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ProblemDetail> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) { ... }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest request) {
        // log ex mas não expor stack trace
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        return ResponseEntity.status(500).body(problem);
    }
}
```

`DemoController` com os quatro endpoints de demonstração.

---

## 06 — Pagination

**Porta:** 8006
**Dependências extras:** `spring-boot-starter-data-jpa`, `com.h2database:h2`

**Estrutura:**
```
src/main/java/com/archlab/sample06/
  common/
    PagedResult.java
  domain/
    Product.java
  infrastructure/
    ProductJpaRepository.java   // extends JpaRepository
    DataSeeder.java             // @Component + ApplicationRunner
  application/
    ProductService.java
  controllers/
    ProductsController.java
Application.java
```

`PagedResult<T>`:
```java
public record PagedResult<T>(
    List<T> items,
    int page,
    int pageSize,
    long totalCount,
    int totalPages,
    boolean hasNextPage,
    boolean hasPreviousPage
) {
    public static <T> PagedResult<T> from(Page<T> springPage) {
        return new PagedResult<>(
            springPage.getContent(),
            springPage.getNumber(),       // zero-indexed
            springPage.getSize(),
            springPage.getTotalElements(),
            springPage.getTotalPages(),
            springPage.hasNext(),
            springPage.hasPrevious()
        );
    }
}
```

Endpoint:
```
GET /api/products?page=0&size=10
```

**Nota importante no README:** Spring Pageable usa índice zero (`page=0` é a primeira página), diferente do .NET onde `page=1` é a primeira.

`DataSeeder`:
```java
@Component
public class DataSeeder implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        // inserir 100 produtos se o banco estiver vazio
    }
}
```

---

## 07 — Unit Testing

**Porta:** 8007 (não precisa subir, é só testes)
**Dependências extras:** `org.mockito:mockito-core` (já incluído via `spring-boot-starter-test`)

**Estrutura:**
```
src/
  main/java/com/archlab/sample07/
    domain/
      Product.java
      StockMovement.java
      MovementType.java    // enum ENTRY(1), EXIT(2)
    application/
      ProductRepository.java   // interface
      StockMovementService.java
      contracts/
        CreateMovementRequest.java
        MovementResponse.java
  test/java/com/archlab/sample07/
    application/
      StockMovementServiceTest.java
```

`StockMovementServiceTest`:
```java
@ExtendWith(MockitoExtension.class)
class StockMovementServiceTest {

    @Mock ProductRepository productRepository;
    StockMovementService service;

    @BeforeEach void setUp() {
        service = new StockMovementService(productRepository);
    }

    @Test void createEntry_increasesBalance() { ... }
    @Test void createExit_decreasesBalance() { ... }
    @Test void createExit_withInsufficientBalance_throwsException() { ... }
    @Test void createExit_withExactBalance_succeeds() { ... }
    @Test void create_withInvalidQuantity_throwsException() { ... }
    @Test void create_withUnknownProduct_throwsNotFoundException() { ... }
}
```

Regras de negócio **idênticas** ao sample C#:
- entrada aumenta `currentBalance`
- saída diminui `currentBalance`
- saída com saldo insuficiente lança exceção com mensagem `"Insufficient stock balance."`
- quantidade <= 0 lança exceção
- produto não encontrado lança `NotFoundException`

---

## 08 — Auth Basics (JWT)

**Porta:** 8008
**Dependências extras:** `spring-boot-starter-security`, `io.jsonwebtoken:jjwt-api:0.12.6`, `jjwt-impl`, `jjwt-jackson`, `org.springframework.security:spring-security-crypto`

**Estrutura:**
```
src/main/java/com/archlab/sample08/
  domain/
    AppUser.java
  application/
    UserRepository.java
    AuthService.java
    contracts/
      RegisterRequest.java
      LoginRequest.java
      AuthResponse.java
      UserProfileResponse.java
  infrastructure/
    InMemoryUserRepository.java
    security/
      JwtTokenProvider.java
      JwtAuthenticationFilter.java
  config/
    SecurityConfig.java
    JwtConfig.java
  controllers/
    AuthController.java
    ProfileController.java
Application.java
```

Implementação idêntica ao `StockFlow.NoSQL.MobileFirst` para auth — reusar a mesma estrutura de `JwtTokenProvider` e `SecurityConfig`.

Endpoints:
```
POST /api/auth/register   → 201 → UserProfileResponse
POST /api/auth/login      → 200 → AuthResponse
GET  /api/profile         → 200 → { id, email, fullName }  [autenticado]
```

`ProfileController`:
```java
@GetMapping("/api/profile")
public UserProfileResponse profile(Authentication authentication) {
    // extrair claims do Authentication object
}
```

---

## 09 — Logging

**Porta:** 8009
**Dependências extras:** `logstash-logback-encoder` para JSON output

**Dependência no `pom.xml`:**
```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

**Estrutura:**
```
src/main/
  java/com/archlab/sample09/
    filter/
      CorrelationIdFilter.java   // OncePerRequestFilter + MDC
      RequestLoggingFilter.java  // OncePerRequestFilter
    service/
      ProductService.java        // demonstra os 4 níveis de log
    controller/
      ProductsController.java
  resources/
    logback-spring.xml
Application.java
```

`logback-spring.xml`:
```xml
<configuration>
    <springProfile name="default">
        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
        </appender>
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>logs/app.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>logs/app-%d{yyyy-MM-dd}.log</fileNamePattern>
            </rollingPolicy>
            <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
        </appender>
        <root level="DEBUG">
            <appender-ref ref="CONSOLE"/>
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>
</configuration>
```

`CorrelationIdFilter`:
```java
public class CorrelationIdFilter extends OncePerRequestFilter {
    private static final String HEADER = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String correlationId = Optional.ofNullable(request.getHeader(HEADER))
                .orElse(UUID.randomUUID().toString());
        MDC.put("correlationId", correlationId);
        response.setHeader(HEADER, correlationId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
```

`ProductService` com os 4 níveis:
```java
@Slf4j
@Service
public class ProductService {
    public ProductResponse findById(String id) {
        log.debug("Fetching product {}", id);
        // buscar...
        log.info("Product {} retrieved successfully", id);
        if (!product.isActive()) log.warn("Product {} is inactive", id);
        return toResponse(product);
    }
}
```

---

## 10 — CQRS Basics

**Porta:** 8010
**Dependências extras:** nenhuma (implementação manual — sem Axon)

**Estrutura:**
```
src/main/java/com/archlab/sample10/
  cqrs/
    Command.java           // interface marcadora
    Query.java             // interface marcadora
    CommandHandler.java    // interface genérica
    QueryHandler.java      // interface genérica
    CommandBus.java        // interface
    QueryBus.java          // interface
    SimpleCommandBus.java  // @Component, descobre handlers via Spring
    SimpleQueryBus.java    // @Component, descobre handlers via Spring
  application/
    commands/
      CreateProductCommand.java
      CreateProductCommandHandler.java
    queries/
      GetProductByIdQuery.java
      GetProductByIdQueryHandler.java
      GetProductsQuery.java
      GetProductsQueryHandler.java
    contracts/
      CreateProductRequest.java
      ProductResponse.java
  infrastructure/
    InMemoryProductRepository.java
  controllers/
    ProductsController.java
Application.java
```

Interfaces CQRS:
```java
public interface Command {}
public interface Query<R> {}

public interface CommandHandler<C extends Command, R> {
    R handle(C command);
}

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
```

`SimpleCommandBus` — descobre todos os `CommandHandler` beans via Spring e despacha:
```java
@Component
public class SimpleCommandBus implements CommandBus {
    private final Map<Class<?>, CommandHandler<?, ?>> handlers;

    public SimpleCommandBus(List<CommandHandler<?, ?>> handlerList) {
        this.handlers = handlerList.stream()
            .collect(Collectors.toMap(h -> resolveCommandType(h.getClass()), h -> h));
    }

    @SuppressWarnings("unchecked")
    public <C extends Command, R> R send(C command) {
        var handler = (CommandHandler<C, R>) handlers.get(command.getClass());
        if (handler == null) throw new IllegalArgumentException("No handler for " + command.getClass());
        return handler.handle(command);
    }
}
```

`ProductsController`:
```java
@PostMapping
public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
    var command = new CreateProductCommand(request.name(), request.sku(), request.unitPrice());
    var result = commandBus.send(command);
    return ResponseEntity.status(201).body(result);
}
```

README deve explicar: por que não Axon (pesado para samples), e comparar com MediatR.

---

## 11 — Result Pattern

**Porta:** 8011
**Dependências extras:** nenhuma

**Estrutura:**
```
src/main/java/com/archlab/sample11/
  common/
    Result.java
    sealed/
      Success.java   // record extends ResultBase<T>
      Failure.java   // record extends ResultBase<T>
  application/
    ProductService.java
  controllers/
    ProductsController.java
Application.java
```

`Result<T>` (versão com classe):
```java
public class Result<T> {
    private final boolean success;
    private final T value;
    private final String error;
    private final String errorCode;

    private Result(T value) { this.success = true; this.value = value; this.error = null; this.errorCode = null; }
    private Result(String error, String errorCode) { this.success = false; this.value = null; this.error = error; this.errorCode = errorCode; }

    public static <T> Result<T> success(T value) { return new Result<>(value); }
    public static <T> Result<T> failure(String error, String errorCode) { return new Result<>(error, errorCode); }

    public boolean isSuccess() { return success; }
    public T getValue() { return value; }
    public String getError() { return error; }
    public String getErrorCode() { return errorCode; }
}
```

Versão com sealed classes (Java 17+) como alternativa no mesmo sample:
```java
public sealed interface ResultBase<T> permits Success, Failure {}
public record Success<T>(T value) implements ResultBase<T> {}
public record Failure<T>(String error, String errorCode) implements ResultBase<T> {}
```

Controller com pattern matching:
```java
return switch (result) {
    case Success<ProductResponse> s -> ResponseEntity.ok(s.value());
    case Failure<ProductResponse> f -> switch (f.errorCode()) {
        case "sku_conflict" -> ResponseEntity.status(409).body(Map.of("detail", f.error()));
        default -> ResponseEntity.badRequest().body(Map.of("detail", f.error()));
    };
};
```

README deve comparar: Java sealed + pattern matching vs. C# records + switch expression — e quando Java finalmente se aproxima da elegância do C# aqui.

---

## 12 — Outbox Pattern

**Porta:** 8012
**Dependências extras:** `spring-boot-starter-data-jpa`, `com.h2database:h2`

**Estrutura:**
```
src/main/java/com/archlab/sample12/
  domain/
    Product.java
  infrastructure/
    OutboxMessage.java       // @Entity
    OutboxRepository.java    // extends JpaRepository
    ProductJpaRepository.java
    OutboxPublisher.java     // @Scheduled(fixedDelay = 5000)
  application/
    ProductService.java      // @Transactional
  controllers/
    ProductsController.java
    OutboxController.java    // GET /api/outbox
Application.java
```

`OutboxMessage`:
```java
@Entity
@Table(name = "outbox_messages")
public class OutboxMessage {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String eventType;
    @Column(columnDefinition = "TEXT")
    private String payload;
    private Instant createdAtUtc;
    private Instant processedAtUtc;   // null = pendente
}
```

`ProductService.create`:
```java
@Transactional
public ProductResponse create(CreateProductRequest request) {
    var product = new Product(/* ... */);
    productRepository.save(product);

    var outboxMessage = new OutboxMessage();
    outboxMessage.setEventType("ProductCreated");
    outboxMessage.setPayload(objectMapper.writeValueAsString(Map.of("id", product.getId(), "name", product.getName())));
    outboxMessage.setCreatedAtUtc(Instant.now());
    outboxRepository.save(outboxMessage);

    return toResponse(product);
}
```

`OutboxPublisher`:
```java
@Component
public class OutboxPublisher {
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutbox() {
        var pending = outboxRepository.findByProcessedAtUtcIsNullOrderByCreatedAtUtcAsc(PageRequest.of(0, 10));
        for (var message : pending) {
            log.info("Publishing {}: {}", message.getEventType(), message.getPayload());
            message.setProcessedAtUtc(Instant.now());
            outboxRepository.save(message);
        }
    }
}
```

**Habilitar scheduling em `Application.java`:**
```java
@SpringBootApplication
@EnableScheduling
public class Application { ... }
```

---

## README raiz do repositório

O `README.md` da raiz deve:
- Listar todos os 12 samples com uma linha de descrição e link para a pasta
- Ter seção "How to run any sample":
  ```bash
  cd samples/01-dependency-injection
  mvn spring-boot:run
  # Swagger: http://localhost:8001/swagger-ui.html
  ```
- Ter seção "Comparison with C#" com link para `CSharp.ArchitectureLab`

---

## Critério de conclusão

A implementação está concluída quando:

1. Todos os 12 samples compilam com `mvn clean package`
2. Todos os samples sobem com `mvn spring-boot:run`
3. Os testes do sample 07 passam com `mvn test`
4. Cada sample tem um `README.md` com objetivo, como rodar, o que observar e comparação com C#
5. O `README.md` raiz lista todos os samples com descrições
