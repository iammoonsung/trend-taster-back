# 오늘의신상 - Backend API

SpringBoot + JPA + PostgreSQL 기반 백엔드 API 서버

## 🏗️ 기술 스택

- **Framework**: Spring Boot 3.3.5
- **Language**: Java 21
- **Build Tool**: Gradle
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA + QueryDSL
- **Security**: Spring Security + JWT
- **Server**: Undertow
- **Documentation**: SpringDoc OpenAPI

## 📋 주요 기능

### 인증 (Authentication)
- ✅ 회원가입 (POST `/api/auth/register`)
- ✅ 로그인 (POST `/api/auth/login`)
- ✅ 현재 사용자 정보 (GET `/api/auth/me`)
- ✅ 로그아웃 (POST `/api/auth/logout`)

### 제품 (Products)
- ✅ 제품 목록 조회 - 필터링 지원 (GET `/api/products`)
- ✅ 제품 상세 조회 (GET `/api/products/{id}`)
- ✅ 제품 등록 (POST `/api/products`) - 인증 필요
- ✅ 제품 수정 (PATCH `/api/products/{id}`) - 인증 필요
- ✅ 제품 삭제 (DELETE `/api/products/{id}`) - 인증 필요

### 관리자 (Admin)
- ✅ 대기 중인 제출 목록 (GET `/api/admin/submissions`) - 관리자 권한 필요
- ✅ 제출 승인 (POST `/api/admin/submissions/{id}/approve`) - 관리자 권한 필요
- ✅ 제출 거부 (POST `/api/admin/submissions/{id}/reject`) - 관리자 권한 필요

## 🚀 시작하기

### 사전 요구사항

- Java 21
- PostgreSQL 14+
- Gradle (wrapper 포함)

### 데이터베이스 설정

```bash
# PostgreSQL 데이터베이스 생성
createdb newproduct

# 또는 psql로
psql
CREATE DATABASE newproduct;
\q
```

### 환경 변수 설정

application.yml에서 다음 환경 변수를 설정하거나 기본값 사용:

```yaml
DB_URL=jdbc:postgresql://localhost:5432/newproduct
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-secret-key-min-256-bits-for-hs256-algorithm
```

### 빌드 및 실행

```bash
# 빌드
./gradlew clean build

# 실행
./gradlew bootRun

# 또는 JAR로 실행
java -jar build/libs/new-product-backend-0.0.1-SNAPSHOT.jar
```

서버가 `http://localhost:8080`에서 실행됩니다.

## 📁 프로젝트 구조

```
src/main/java/com/newproduct/backend/
├── config/              # 설정 클래스 (Security, CORS)
├── controller/          # REST API 컨트롤러
├── domain/              # JPA 엔티티
├── dto/                 # 요청/응답 DTO
├── exception/           # 예외 처리
├── repository/          # JPA Repository
├── security/            # JWT 인증/인가
└── service/             # 비즈니스 로직
```

## 🗄️ 데이터베이스 스키마

### Users 테이블
- id (PK)
- username (unique)
- email (unique)
- password (hashed)
- role (USER, ADMIN, SUPER_ADMIN)
- created_at, updated_at

### Products 테이블
- id (PK)
- name
- store (편의점/브랜드)
- price
- category
- release_date
- description
- ingredients
- barcode
- location
- status (PENDING, APPROVED, REJECTED)
- submitted_by (FK -> users)
- views_count
- created_at, updated_at

### Product_Images 테이블
- id (PK)
- product_id (FK -> products)
- image_url
- display_order
- created_at, updated_at

## 🔐 인증

JWT (JSON Web Token) 기반 인증 사용

### 요청 헤더
```
Authorization: Bearer <JWT_TOKEN>
```

### 토큰 유효기간
- 24시간 (86400000ms)

## 🌐 CORS 설정

프론트엔드와 통신을 위한 CORS 허용:
- `http://localhost:3000`
- `http://localhost:3001`

## 📝 API 문서

서버 실행 후 Swagger UI 접속:
- http://localhost:8080/swagger-ui/index.html

## 🧪 테스트

```bash
./gradlew test
```

## 🔧 개발 도구

- **QueryDSL**: 타입 안전한 쿼리
- **MapStruct**: DTO 매핑
- **Lombok**: 보일러플레이트 코드 제거
- **Spring Boot DevTools**: 핫 리로드

## 📄 라이선스

Private Project
