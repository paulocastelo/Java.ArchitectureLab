# Auditoria — Java.ArchitectureLab

Data: 2026-04-07

## Resultado geral

Implementação aprovada com **2 desvios encontrados**. Os demais 10 samples estão em conformidade com o spec.

---

## Checklist por sample

### 01 — Dependency Injection ⚠️ DESVIO

- `@Scope("prototype")` → equivalente ao Transient do .NET — correto no conceito
- `@Scope("request")` → equivalente ao Scoped do .NET — correto no conceito
- Singleton default do Spring → correto
- Controller injeta dois de cada tipo para demonstrar comportamento — correto na estrutura

#### Desvio 1 — Prototype beans num controller Singleton não são re-criados por request

**Arquivo:** `samples/01-dependency-injection/src/main/java/com/archlab/sample01/controllers/LifetimeController.java`

`LifetimeController` é um bean Singleton por padrão no Spring. Quando um bean Prototype é injetado via construtor num Singleton, o Spring cria **uma única instância** do prototype no momento da criação do controller, e essa instância nunca é substituída. O endpoint sempre retornará os mesmos IDs para `prototype1` e `prototype2`, o que não demonstra o comportamento "nova instância por injeção/request".

A demo funciona parcialmente: mostra que `prototype1 ≠ prototype2` (dois IDs distintos em startup). Mas o objetivo de mostrar IDs diferentes a cada request não é atingido.

**Correção recomendada:** substituir a injeção direta por `ObjectProvider<PrototypeGreetingService>`:

```java
// Injetar providers em vez das instâncias diretas
private final ObjectProvider<PrototypeGreetingService> prototypeProvider;

@GetMapping
public Map<String, String> show() {
    var prototype1 = prototypeProvider.getObject();
    var prototype2 = prototypeProvider.getObject();
    // prototype1.greet() e prototype2.greet() agora têm IDs distintos a cada request
}
```

### 02 — Repository Pattern ✅
- Interface `ProductRepository` com implementações `JpaProductRepository` (Spring Data JPA + H2) e `InMemoryProductRepository` — correto
- `ProductService` depende apenas da interface — correto
- `AppJpaRepository extends JpaRepository<Product, Long>` — correto

### 03 — DTO Mapping ✅
- `ProductManualMapper` com método estático (equivalente às extension methods do C#) — correto
- `ProductMapper` com MapStruct (`@Mapper`) — correto
- Dois endpoints demonstrando as duas abordagens — correto

### 04 — Validation ✅
- `CreateProductRequest` com Bean Validation (`@NotBlank`, `@Positive`) — correto
- `@StrongPassword` como constraint customizada com `StrongPasswordValidator implements ConstraintValidator` — correto
- `StrongPasswordValidator` valida comprimento mínimo 8, uppercase e dígito — correto
- `GlobalExceptionHandler` captura `MethodArgumentNotValidException` para retornar erros estruturados — correto

### 05 — Error Handling ✅
- `@RestControllerAdvice` com handlers separados por tipo de exceção — correto
- NotFoundException→404, ConflictException→409, BusinessRuleException→422, Exception→500 — correto
- Retorna `ProblemDetail` com propriedades `type` e `traceId` — correto
- `DemoController` com 4 endpoints de demonstração — correto

### 06 — Pagination ✅
- `PagedResult<T>` como Java `record` com factory method `from(Page<T>)` — correto
- Utiliza `Page<T>` do Spring Data para popular `hasNextPage` e `hasPreviousPage` — correto
- `DataSeeder` para popular dados de demonstração — correto
- Paginação 0-based (convenção do Spring Data) — correto

### 07 — Unit Testing ✅
- `@ExtendWith(MockitoExtension.class)` com `@Mock` e `@BeforeEach` — correto
- 6 casos de teste: entry aumenta saldo, exit diminui, saldo insuficiente lança `IllegalStateException`, saída com saldo exato funciona, quantidade zero lança `IllegalArgumentException`, produto desconhecido lança `NotFoundException` — correto
- Testes síncronos (correto para Java) — correto
- Estrutura AAA em todos os testes — correto

### 08 — Auth Basics ⚠️ DESVIO

- `JwtTokenProvider` com jjwt 0.12.x (API fluente moderna) — correto
- `JwtAuthenticationFilter extends OncePerRequestFilter` — correto
- `SecurityConfig` com CSRF desabilitado, sessão stateless, `@EnableMethodSecurity` — correto
- `ProfileController` protegido, lê claims do `Authentication` — correto

#### Desvio 2 — HTTP Basic Auth habilitado desnecessariamente

**Arquivo:** `samples/08-auth-basics/src/main/java/com/archlab/sample08/config/SecurityConfig.java:29`

```java
// atual (desnecessário)
.httpBasic(Customizer.withDefaults())

// esperado
.httpBasic(basic -> basic.disable())
```

O sample demonstra autenticação JWT. Deixar o HTTP Basic habilitado é enganoso — sugere que Basic Auth também funciona, quando o objetivo é mostrar JWT exclusivamente. Deve ser explicitamente desabilitado.

### 09 — Logging ✅
- `logback-spring.xml` com `LogstashEncoder` (JSON estruturado) no console e arquivo rolling diário — correto
- `CorrelationIdFilter` lê `X-Correlation-Id` do request (ou gera novo UUID), adiciona ao `MDC` e propaga na response — correto
- `MDC.clear()` no bloco `finally` (evita vazamento entre threads do pool) — correto
- Port 9009 — correto

### 10 — CQRS ✅
- `CommandBus` e `QueryBus` implementados manualmente (Java não tem MediatR) — correto e mais didático
- `SimpleCommandBus` e `SimpleQueryBus` resolvem handlers dinamicamente via `ResolvableType` — correto
- Commands e Queries em pacotes separados — correto
- Controller usa apenas `CommandBus` e `QueryBus`, sem lógica de negócio — correto

### 11 — Result Pattern ✅
- `Result<T>` é `final` (equivalente ao `sealed` do C#) — correto
- Factory methods `success()` e `failure()` com nomes em lowerCamelCase (convenção Java) — correto
- Bonus: pacote `sealed` com `ResultBase`, `Success<T>`, `Failure<T>` usando sealed classes do Java 21 — adição didática válida
- Controller mapeia `errorCode` para status HTTP correto — correto

### 12 — Outbox Pattern ✅
- `ProductService.create()` anotado com `@Transactional` — salva `Product` e `OutboxMessage` na mesma transação — correto
- `OutboxPublisher` com `@Scheduled(fixedDelay = 5000)` (polling a cada 5s) + `@Transactional` — correto
- Processa até 10 mensagens pendentes por ciclo — correto
- `@SpringBootApplication(... @EnableScheduling)` habilitado — correto

---

## Resumo dos ajustes necessários

| # | Sample | Arquivo | Ajuste |
|---|--------|---------|--------|
| 1 | 01-dependency-injection | `controllers/LifetimeController.java` | Substituir injeção direta de Prototype por `ObjectProvider<PrototypeGreetingService>` |
| 2 | 08-auth-basics | `config/SecurityConfig.java:29` | Trocar `.httpBasic(Customizer.withDefaults())` por `.httpBasic(basic -> basic.disable())` |

---

## Observação sobre o CQRS (Sample 10)

A implementação Java é deliberadamente mais rica que o equivalente C#: em vez de usar MediatR (que não existe em Java), foi implementado um `CommandBus`/`QueryBus` próprio. Isso é um ponto positivo — demonstra como o padrão funciona por debaixo dos panos, o que o MediatR abstrai. Vale mencionar no README do sample.
