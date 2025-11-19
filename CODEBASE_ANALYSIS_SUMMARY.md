# TrendTaster Backend - Codebase Analysis Summary

## Project Quick Facts

**Project Name:** TrendTaster Backend (오늘의신상)
**Type:** Spring Boot REST API Backend
**Language:** Java 21
**Build Tool:** Gradle
**Database:** PostgreSQL
**Location:** `/Users/moonsung/workspace/trendTasterBack`

## What This Project Does

TrendTaster is a REST API backend for a convenience store product discovery platform where:
- Users can register/login and submit new products
- Products start in PENDING status and require admin approval
- Admins can approve, reject, or manage product submissions
- Users can browse approved products with filtering by store, category, and price
- Stores/brands also require admin approval before products can be submitted to them

## Essential Commands

| Task | Command |
|------|---------|
| Build | `./gradlew clean build` |
| Run | `./gradlew bootRun` |
| Test | `./gradlew test` |
| Skip Tests | `./gradlew build -x test` |
| JAR | `java -jar build/libs/trend-taster-backend-0.0.1-SNAPSHOT.jar` |
| API Docs | http://localhost:8080/swagger-ui/index.html |

## Technology Stack at a Glance

```
Framework & Language
├── Java 21
├── Spring Boot 3.3.5
└── Gradle

Database & Persistence
├── PostgreSQL (driver: postgresql)
├── Spring Data JPA
├── QueryDSL 5.0.0 (type-safe queries)
└── Hiberndate 

Security & Auth
├── Spring Security
├── JWT (jjwt 0.12.3)
└── BCrypt password encoding

Additional Libraries
├── Lombok (boilerplate reduction)
├── MapStruct (DTO mapping)
├── Undertow (servlet container)
└── SpringDoc OpenAPI 2.0.2 (Swagger)
```

## Architecture Overview

**Pattern:** Layered Architecture with clean separation of concerns

```
HTTP Requests
     ↓
┌─────────────────────────────────┐
│  Controllers                    │ - @RestController
│  (Handle HTTP, validate params) │ - OpenAPI annotations
└──────────────┬──────────────────┘
               ↓
┌──────────────────────────────────┐
│  Services                        │ - @Service
│  (Business logic, transactions)  │ - @Transactional
└──────────────┬───────────────────┘
               ↓
┌──────────────────────────────────┐
│  Repositories                    │ - JpaRepository
│  (Database queries & CRUD)       │ - Custom JPQL queries
└──────────────┬───────────────────┘
               ↓
┌──────────────────────────────────┐
│  PostgreSQL Database             │
│  (Tables: users, products, etc)  │
└──────────────────────────────────┘
```

## Key Packages

- **`config/`** - Spring configuration (Security, OpenAPI/Swagger)
- **`controller/`** - 6 REST controllers (Auth, Product, Store, Admin, Upload, Health)
- **`service/`** - 3 services (Auth, Product, Store) - business logic
- **`repository/`** - 4 data access repositories (User, Product, Store, UploadToken)
- **`domain/`** - 6 JPA entities (User, Product, ProductImage, Store, UploadToken, BaseEntity)
- **`dto/`** - 5 DTO classes with request/response inner classes
- **`security/`** - JWT token provider and authentication filter
- **`exception/`** - Global exception handler

**Total:** 30 Java files organized in 9 logical packages

## Core Concepts

### Authentication & Authorization
- **Stateless JWT tokens** (24-hour expiry, HS256 algorithm)
- **Role-based access control**: USER, ADMIN, SUPER_ADMIN
- **Password hashing**: BCryptPasswordEncoder
- Token extracted from `Authorization: Bearer <token>` header

### Data Model Key Points
- All entities extend `BaseEntity` (JPA Auditing with createdAt/updatedAt)
- Product statuses: PENDING → APPROVED/REJECTED (approval workflow)
- User roles: USER → ADMIN → SUPER_ADMIN (promotion hierarchy)
- Store statuses: PENDING → APPROVED/REJECTED (approval workflow)
- Products have One-to-Many relationship with ProductImages

### Database Features
- 5+ custom JPQL queries for filtering/search
- Database indexes on frequently queried columns
- Unique constraints on email, username, store names
- Cascade delete for orphaned images
- Lazy loading for relationships

## Important Configuration

| Setting | Value | Notes |
|---------|-------|-------|
| Server Port | 8080 | Undertow servlet container |
| Database | PostgreSQL on localhost:5432 | Database: `newproduct` |
| JWT Expiry | 86400000 ms (24h) | Configurable via JWT_SECRET env var |
| Max File Size | 10MB | Multipart upload limit |
| CORS Origins | localhost:3000, localhost:3001 | Frontend dev origins |
| Hibernate DDL | `update` | Auto-creates/updates schema |

## API Organization

### Public Endpoints (No Auth Required)
- `POST /api/auth/register` - User signup
- `POST /api/auth/login` - Get JWT token
- `GET /api/products` - List products (paginated, filterable)
- `GET /api/products/{id}` - Get product details

### Authenticated Endpoints (User Must Be Logged In)
- `GET /api/auth/me` - Current user info
- `POST /api/products` - Submit new product
- `PATCH /api/products/{id}` - Update own product
- `DELETE /api/products/{id}` - Delete own product

### Admin-Only Endpoints (Requires ADMIN Role)
- `GET /api/admin/submissions` - View pending products
- `POST /api/admin/submissions/{id}/approve` - Approve product
- `POST /api/admin/submissions/{id}/reject` - Reject product
- `GET /api/admin/stats` - Dashboard statistics
- `POST /api/admin/users/{id}/promote` - Grant admin role
- Similar endpoints for Store management

### Documentation
- `GET /swagger-ui/index.html` - Interactive API explorer
- `GET /v3/api-docs/**` - OpenAPI JSON specification

## Code Style Patterns Used

### Lombok Annotations
```java
@Getter @Setter                                    // Auto getters/setters
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA requirement
@AllArgsConstructor                                // Builder support
@Builder                                           // Fluent creation
@RequiredArgsConstructor                           // Constructor injection
@Slf4j                                             // Logger instance
```

### Entity Patterns
```java
public class Product extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private User submittedBy;
    
    // Business methods
    public void approve() { this.status = ProductStatus.APPROVED; }
    public void reject() { this.status = ProductStatus.REJECTED; }
}
```

### Service Patterns
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    
    @Transactional  // Override to enable writes
    public ProductDto.Response createProduct(...) {
        // Business logic with validation
        Product product = Product.builder()...build();
        return ProductDto.Response.from(productRepository.save(product));
    }
}
```

### DTO Patterns
```java
public class ProductDto {
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Product name required")
        private String name;
        // ... other fields with validation
    }
    
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String name;
        // ... response fields only
        
        public static Response from(Product product) {
            return Response.builder()
                .id(product.getId())
                .name(product.getName())
                // ... map other fields
                .build();
        }
    }
}
```

## Notable Design Decisions

1. **Stateless JWT Auth** - No sessions, clean REST design, scalable
2. **Entity Status Enums** - Type-safe approval workflow states
3. **Lazy Loading** - Prevents N+1 queries on relationships
4. **DTO Pattern** - API contracts separate from entities
5. **JPA Auditing** - Automatic createdAt/updatedAt tracking
6. **QueryDSL Ready** - Set up for type-safe queries (not heavily used currently)
7. **Exception Handling** - Centralized via @RestControllerAdvice
8. **OpenAPI/Swagger** - Auto-generated API documentation

## Known Gaps & TODOs

1. No unit/integration tests (test folder empty)
2. Update methods (PATCH) are incomplete placeholders
3. No pagination for admin submission lists
4. File upload service incomplete (token mechanism exists but not fully implemented)
5. No soft deletes for audit trail
6. Static index.html likely not needed

## Environment Setup

Required environment variables:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/newproduct
export DB_USERNAME=postgres
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_256_bit_secret_key_minimum
```

## File Organization Key Insights

- **30 Java files** across 9 logical packages
- **6 controllers** handling different domain areas
- **3 services** managing business logic
- **4 repositories** with custom queries
- **6 domain entities** with inheritance
- **5 DTO classes** with inner static classes for different operations
- **1 exception handler** for global error handling
- **2 config classes** for security and API docs

## Why This Structure Works

1. **Clear Separation** - Controllers don't know about database, services don't know about HTTP
2. **Testability** - Layers can be tested independently with mocks
3. **Maintainability** - Related code grouped by concern (auth, products, admin)
4. **Scalability** - Easy to add new features following existing patterns
5. **Type Safety** - Enums for statuses, validated DTOs, compile-time checks
6. **Security** - JWT tokens, password hashing, role-based access, SQL injection prevention

## Future Architecture Improvements

- Add service-to-service event bus (Spring Events or message queue)
- Implement caching layer (Redis) for frequently accessed products
- Add audit logging (Javers) for compliance/tracking
- Implement full-text search for products
- Add notification service (email/push)
- Separate read and write models (CQRS) if scaling becomes necessary

## Quick Cheat Sheet

```bash
# Setup
cd /Users/moonsung/workspace/trendTasterBack
export DB_URL=jdbc:postgresql://localhost:5432/newproduct
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
export JWT_SECRET=your_256_bit_key

# Build & Run
./gradlew clean build
./gradlew bootRun

# Test
./gradlew test

# View API Docs
# Visit: http://localhost:8080/swagger-ui/index.html

# Build JAR
./gradlew build
java -jar build/libs/trend-taster-backend-0.0.1-SNAPSHOT.jar
```

---

This codebase demonstrates professional Spring Boot patterns with clean architecture, proper separation of concerns, and production-ready security practices. The code is well-organized, follows Java conventions, and provides a solid foundation for the TrendTaster product discovery platform.

**Generated:** 2025-11-19  
**Analysis Tool:** Claude Code - File Search & Analysis  
**Confidence Level:** High (comprehensive codebase review)
