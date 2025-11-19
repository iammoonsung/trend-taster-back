# Complete List of Files Updated

## Java Source Files (30 files)

### Root Package (1 file)
1. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/NewProductBackendApplication.java`
   - Package: `com.newproduct.backend` → `com.trendtaster.backend`

### Domain Package (6 files)
2. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/domain/BaseEntity.java`
   - Package: `com.newproduct.backend.domain` → `com.trendtaster.backend.domain`

3. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/domain/User.java`
   - Package: `com.newproduct.backend.domain` → `com.trendtaster.backend.domain`

4. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/domain/Product.java`
   - Package: `com.newproduct.backend.domain` → `com.trendtaster.backend.domain`

5. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/domain/ProductImage.java`
   - Package: `com.newproduct.backend.domain` → `com.trendtaster.backend.domain`

6. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/domain/Store.java`
   - Package: `com.newproduct.backend.domain` → `com.trendtaster.backend.domain`

7. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/domain/UploadToken.java`
   - Package: `com.newproduct.backend.domain` → `com.trendtaster.backend.domain`

### DTO Package (5 files)
8. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/dto/AuthDto.java`
   - Package: `com.newproduct.backend.dto` → `com.trendtaster.backend.dto`
   - Import: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`

9. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/dto/ProductDto.java`
   - Package: `com.newproduct.backend.dto` → `com.trendtaster.backend.dto`
   - Imports: `com.newproduct.backend.domain.Product` → `com.trendtaster.backend.domain.Product`
   - Imports: `com.newproduct.backend.domain.ProductImage` → `com.trendtaster.backend.domain.ProductImage`

10. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/dto/StoreDto.java`
    - Package: `com.newproduct.backend.dto` → `com.trendtaster.backend.dto`
    - Import: `com.newproduct.backend.domain.Store` → `com.trendtaster.backend.domain.Store`

11. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/dto/AdminDto.java`
    - Package: `com.newproduct.backend.dto` → `com.trendtaster.backend.dto`

12. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/dto/UploadDto.java`
    - Package: `com.newproduct.backend.dto` → `com.trendtaster.backend.dto`

### Security Package (2 files)
13. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/security/JwtTokenProvider.java`
    - Package: `com.newproduct.backend.security` → `com.trendtaster.backend.security`

14. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/security/JwtAuthenticationFilter.java`
    - Package: `com.newproduct.backend.security` → `com.trendtaster.backend.security`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.repository.UserRepository` → `com.trendtaster.backend.repository.UserRepository`

### Service Package (3 files)
15. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/service/AuthService.java`
    - Package: `com.newproduct.backend.service` → `com.trendtaster.backend.service`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.dto.AuthDto` → `com.trendtaster.backend.dto.AuthDto`
    - Imports: `com.newproduct.backend.repository.UserRepository` → `com.trendtaster.backend.repository.UserRepository`
    - Imports: `com.newproduct.backend.security.JwtTokenProvider` → `com.trendtaster.backend.security.JwtTokenProvider`

16. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/service/StoreService.java`
    - Package: `com.newproduct.backend.service` → `com.trendtaster.backend.service`
    - Imports: `com.newproduct.backend.domain.Store` → `com.trendtaster.backend.domain.Store`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.dto.StoreDto` → `com.trendtaster.backend.dto.StoreDto`
    - Imports: `com.newproduct.backend.repository.StoreRepository` → `com.trendtaster.backend.repository.StoreRepository`

17. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/service/ProductService.java`
    - Package: `com.newproduct.backend.service` → `com.trendtaster.backend.service`
    - Imports: `com.newproduct.backend.domain.Product` → `com.trendtaster.backend.domain.Product`
    - Imports: `com.newproduct.backend.domain.ProductImage` → `com.trendtaster.backend.domain.ProductImage`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.dto.ProductDto` → `com.trendtaster.backend.dto.ProductDto`
    - Imports: `com.newproduct.backend.repository.ProductRepository` → `com.trendtaster.backend.repository.ProductRepository`

### Repository Package (4 files)
18. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/repository/UserRepository.java`
    - Package: `com.newproduct.backend.repository` → `com.trendtaster.backend.repository`
    - Import: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`

19. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/repository/ProductRepository.java`
    - Package: `com.newproduct.backend.repository` → `com.trendtaster.backend.repository`
    - Import: `com.newproduct.backend.domain.Product` → `com.trendtaster.backend.domain.Product`

20. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/repository/StoreRepository.java`
    - Package: `com.newproduct.backend.repository` → `com.trendtaster.backend.repository`
    - Import: `com.newproduct.backend.domain.Store` → `com.trendtaster.backend.domain.Store`

21. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/repository/UploadTokenRepository.java`
    - Package: `com.newproduct.backend.repository` → `com.trendtaster.backend.repository`
    - Import: `com.newproduct.backend.domain.UploadToken` → `com.trendtaster.backend.domain.UploadToken`

### Config Package (2 files)
22. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/config/OpenApiConfig.java`
    - Package: `com.newproduct.backend.config` → `com.trendtaster.backend.config`

23. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/config/SecurityConfig.java`
    - Package: `com.newproduct.backend.config` → `com.trendtaster.backend.config`
    - Import: `com.newproduct.backend.security.JwtAuthenticationFilter` → `com.trendtaster.backend.security.JwtAuthenticationFilter`

### Controller Package (6 files)
24. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/controller/HealthController.java`
    - Package: `com.newproduct.backend.controller` → `com.trendtaster.backend.controller`

25. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/controller/AuthController.java`
    - Package: `com.newproduct.backend.controller` → `com.trendtaster.backend.controller`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.dto.AuthDto` → `com.trendtaster.backend.dto.AuthDto`
    - Imports: `com.newproduct.backend.service.AuthService` → `com.trendtaster.backend.service.AuthService`

26. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/controller/ProductController.java`
    - Package: `com.newproduct.backend.controller` → `com.trendtaster.backend.controller`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.dto.ProductDto` → `com.trendtaster.backend.dto.ProductDto`
    - Imports: `com.newproduct.backend.service.ProductService` → `com.trendtaster.backend.service.ProductService`

27. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/controller/StoreController.java`
    - Package: `com.newproduct.backend.controller` → `com.trendtaster.backend.controller`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.dto.StoreDto` → `com.trendtaster.backend.dto.StoreDto`
    - Imports: `com.newproduct.backend.service.StoreService` → `com.trendtaster.backend.service.StoreService`

28. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/controller/AdminController.java`
    - Package: `com.newproduct.backend.controller` → `com.trendtaster.backend.controller`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.dto.AdminDto` → `com.trendtaster.backend.dto.AdminDto`
    - Imports: `com.newproduct.backend.dto.AuthDto` → `com.trendtaster.backend.dto.AuthDto`
    - Imports: `com.newproduct.backend.dto.ProductDto` → `com.trendtaster.backend.dto.ProductDto`
    - Imports: `com.newproduct.backend.dto.StoreDto` → `com.trendtaster.backend.dto.StoreDto`
    - Imports: `com.newproduct.backend.repository.ProductRepository` → `com.trendtaster.backend.repository.ProductRepository`
    - Imports: `com.newproduct.backend.repository.UserRepository` → `com.trendtaster.backend.repository.UserRepository`
    - Imports: `com.newproduct.backend.service.ProductService` → `com.trendtaster.backend.service.ProductService`
    - Imports: `com.newproduct.backend.service.StoreService` → `com.trendtaster.backend.service.StoreService`

29. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/controller/UploadController.java`
    - Package: `com.newproduct.backend.controller` → `com.trendtaster.backend.controller`
    - Imports: `com.newproduct.backend.domain.UploadToken` → `com.trendtaster.backend.domain.UploadToken`
    - Imports: `com.newproduct.backend.domain.User` → `com.trendtaster.backend.domain.User`
    - Imports: `com.newproduct.backend.dto.UploadDto` → `com.trendtaster.backend.dto.UploadDto`
    - Imports: `com.newproduct.backend.repository.UploadTokenRepository` → `com.trendtaster.backend.repository.UploadTokenRepository`

### Exception Package (1 file)
30. `/Users/moonsung/workspace/newProductBack/src/main/java/com/newproduct/backend/exception/GlobalExceptionHandler.java`
    - Package: `com.newproduct.backend.exception` → `com.trendtaster.backend.exception`

## Configuration Files (1 file)

31. `/Users/moonsung/workspace/newProductBack/src/main/resources/application.yml`
    - Logging configuration: `com.newproduct.backend` → `com.trendtaster.backend`

## Supporting Files Created

32. `/Users/moonsung/workspace/newProductBack/migrate.sh`
    - Shell script for automated file migration

33. `/Users/moonsung/workspace/newProductBack/migrate_packages.py`
    - Python script for automated file migration (alternative)

34. `/Users/moonsung/workspace/newProductBack/REFACTORING_SUMMARY.md`
    - Comprehensive refactoring documentation

35. `/Users/moonsung/workspace/newProductBack/FILES_UPDATED.md`
    - This file - complete list of updated files

## Total Changes
- **30** Java source files updated with new package declarations
- **30** Java source files updated with new import statements (where applicable)
- **1** Configuration file updated
- **4** Supporting files created for migration and documentation

## Status
✅ All package declarations updated
✅ All import statements updated
✅ Configuration files updated
✅ Migration scripts created
⏳ Awaiting physical file move to new directory structure
