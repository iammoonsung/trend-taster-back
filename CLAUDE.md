# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TrendTaster Backend is a Spring Boot REST API for a convenience store product discovery platform. Users submit new products which administrators approve before public visibility.

**Tech Stack:** Java 21, Spring Boot 3.3.5, PostgreSQL, Gradle, JWT authentication

## Build & Run Commands

```bash
./gradlew clean build      # Build (includes tests)
./gradlew build -x test    # Build without tests
./gradlew bootRun          # Run application
./gradlew test             # Run tests
```

**Server Port:** 8080
**Swagger UI:** http://localhost:8080/swagger-ui/index.html

## Required Environment Variables

```bash
DB_URL=jdbc:postgresql://localhost:5432/newproduct
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your-secret-key-minimum-256-bits-long
```

## Architecture

### Package Structure

```
com.trendTaster
├── controller   # REST endpoints (6 controllers)
├── service      # Business logic with @Transactional
├── repository   # Spring Data JPA + custom JPQL queries
├── domain       # JPA entities extending BaseEntity
├── dto          # Request/Response as inner static classes
├── config       # SecurityConfig, OpenApiConfig
├── security     # JwtTokenProvider, JwtAuthenticationFilter
└── exception    # GlobalExceptionHandler (@RestControllerAdvice)
```

### Key Patterns

- **Stateless JWT Authentication**: Token in Authorization header, 24-hour expiry, HS256 algorithm
- **Role-Based Access Control**: USER, ADMIN, SUPER_ADMIN with `@AuthenticationPrincipal` injection
- **Approval Workflow**: Products/Stores start as PENDING, require admin approval to become APPROVED
- **DTO Pattern**: Inner static classes (e.g., `ProductDto.CreateRequest`, `ProductDto.Response`) with MapStruct conversion
- **Entity Inheritance**: All entities extend `BaseEntity` with auto-managed `createdAt`/`updatedAt`

### Data Flow

1. Controllers receive HTTP requests and delegate to services
2. Services contain business logic with `@Transactional` boundaries
3. Repositories execute JPA queries (including custom JPQL for filtering)
4. DTOs separate API contracts from entities

### Main Entity Relationships

- **User** (roles: USER/ADMIN/SUPER_ADMIN) submits Products and Stores
- **Product** has many ProductImages, references submitter User and Store
- **Store** requires approval before products can reference it

## API Groups

- `/api/auth/*` - Registration, login, user info (public except `/me`)
- `/api/products/*` - Product CRUD with filtering (list/detail public, mutations require auth)
- `/api/admin/*` - Approval workflow, user management (admin only)
- `/health`, `/api/health` - Health checks

## Code Conventions

### Services
- Class-level `@Transactional(readOnly = true)`
- Method-level `@Transactional` for mutations
- Constructor injection via `@RequiredArgsConstructor`

### DTOs
- Request DTOs: validation annotations (`@NotNull`, `@NotBlank`, `@Email`, `@Size`)
- Response DTOs: include `from(Entity)` factory methods

### Controllers
- OpenAPI annotations: `@Operation`, `@ApiResponse`, `@Parameter`
- Return `ResponseEntity` for status code control

## Known Limitations

- **No tests**: Test directory is empty
- **Incomplete update methods**: `ProductService.updateProduct()` and `StoreService.updateStore()` are stubs
- **No pagination for admin lists**: `getPendingSubmissions()` returns all results
- **Development database mode**: `ddl-auto: update` - use migrations in production

## CORS Configuration

Allowed origins: `localhost:3000`, `localhost:3001` (frontend development servers)
