package com.trendTaster.controller;

import com.trendTaster.domain.User;
import com.trendTaster.dto.AdminDto;
import com.trendTaster.dto.AuthDto;
import com.trendTaster.dto.ProductDto;
import com.trendTaster.dto.StoreDto;
import com.trendTaster.repository.ProductRepository;
import com.trendTaster.repository.StoreRepository;
import com.trendTaster.repository.UserRepository;
import com.trendTaster.service.ProductService;
import com.trendTaster.service.ProductUpdateSubmissionService;
import com.trendTaster.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "관리자", description = "관리자 전용 API (제품/매장 승인/거부)")
public class AdminController {

    private final ProductService productService;
    private final ProductUpdateSubmissionService updateSubmissionService;
    private final StoreService storeService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Operation(summary = "대기 중인 제출 목록 조회", description = "승인 대기 중인 제품 목록을 조회합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.Response.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content)
    })
    @GetMapping("/submissions")
    public ResponseEntity<Page<ProductDto.Response>> getPendingSubmissions(
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto.Response> submissions = productService.getPendingSubmissions(pageable);
        return ResponseEntity.ok(submissions);
    }

    @Operation(summary = "제출 승인", description = "대기 중인 제품을 승인합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승인 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.Response.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content),
            @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음", content = @Content)
    })
    @PostMapping("/submissions/{id}/approve")
    public ResponseEntity<ProductDto.Response> approveSubmission(
            @Parameter(description = "제품 ID") @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        ProductDto.Response product = productService.approveProduct(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "제출 거부", description = "대기 중인 제품을 거부합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "거부 성공"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content),
            @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음", content = @Content)
    })
    @PostMapping("/submissions/{id}/reject")
    public ResponseEntity<Void> rejectSubmission(
            @Parameter(description = "제품 ID") @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        productService.rejectProduct(id);
        return ResponseEntity.ok().build();
    }

    // Store Management Endpoints

    @Operation(summary = "대기 중인 매장/브랜드 목록 조회", description = "승인 대기 중인 매장/브랜드 목록을 조회합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = StoreDto.Response.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content)
    })
    @GetMapping("/stores/submissions")
    public ResponseEntity<Page<StoreDto.Response>> getPendingStores(
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<StoreDto.Response> stores = storeService.getPendingStores(pageable);
        return ResponseEntity.ok(stores);
    }

    @Operation(summary = "매장/브랜드 승인", description = "대기 중인 매장/브랜드를 승인합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승인 성공",
                    content = @Content(schema = @Schema(implementation = StoreDto.Response.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content),
            @ApiResponse(responseCode = "404", description = "매장을 찾을 수 없음", content = @Content)
    })
    @PostMapping("/stores/submissions/{id}/approve")
    public ResponseEntity<StoreDto.Response> approveStore(
            @Parameter(description = "매장 ID") @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        StoreDto.Response store = storeService.approveStore(id, user);
        return ResponseEntity.ok(store);
    }

    @Operation(summary = "매장/브랜드 거부", description = "대기 중인 매장/브랜드를 거부합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "거부 성공",
                    content = @Content(schema = @Schema(implementation = StoreDto.Response.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content),
            @ApiResponse(responseCode = "404", description = "매장을 찾을 수 없음", content = @Content)
    })
    @PostMapping("/stores/submissions/{id}/reject")
    public ResponseEntity<StoreDto.Response> rejectStore(
            @Parameter(description = "매장 ID") @PathVariable("id") Long id,
            @RequestBody StoreDto.RejectRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        StoreDto.Response store = storeService.rejectStore(id, request.getReason(), user);
        return ResponseEntity.ok(store);
    }

    // Admin Stats Endpoint

    @Operation(summary = "관리자 통계 조회", description = "관리자 대시보드 통계를 조회합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AdminDto.Stats.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content)
    })
    @GetMapping("/stats")
    public ResponseEntity<AdminDto.Stats> getAdminStats(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        AdminDto.Stats stats = AdminDto.Stats.builder()
                .pendingSubmissions(productRepository.countPendingProducts())
                .approvedProducts(productRepository.countApprovedProducts())
                .pendingStores(storeRepository.countPendingStores())
                .approvedStores(storeRepository.countApprovedStores())
                .pendingUpdateSubmissions(updateSubmissionService.countPendingUpdateSubmissions())
                .totalUsers(userRepository.count())
                .adminUsers(userRepository.countAdminUsers())
                .build();

        return ResponseEntity.ok(stats);
    }

    // User Management Endpoints

    @Operation(summary = "사용자 검색", description = "사용자명 또는 이메일로 사용자를 검색합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AuthDto.UserResponse.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content)
    })
    @GetMapping("/users/search")
    public ResponseEntity<Page<AuthDto.UserResponse>> searchUsers(
            @Parameter(description = "검색어") @RequestParam("q") String query,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.searchUsersPageable(query, pageable);
        Page<AuthDto.UserResponse> response = users.map(AuthDto.UserResponse::from);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "관리자 권한 부여", description = "사용자에게 관리자 권한을 부여합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 부여 성공",
                    content = @Content(schema = @Schema(implementation = AuthDto.UserResponse.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @PostMapping("/users/{id}/promote")
    public ResponseEntity<AuthDto.UserResponse> promoteToAdmin(
            @Parameter(description = "사용자 ID") @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        targetUser.promoteToAdmin();
        userRepository.save(targetUser);

        return ResponseEntity.ok(AuthDto.UserResponse.from(targetUser));
    }

    @Operation(summary = "관리자 권한 제거", description = "사용자의 관리자 권한을 제거합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 제거 성공",
                    content = @Content(schema = @Schema(implementation = AuthDto.UserResponse.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @PostMapping("/users/{id}/demote")
    public ResponseEntity<AuthDto.UserResponse> demoteFromAdmin(
            @Parameter(description = "사용자 ID") @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        targetUser.demoteToUser();
        userRepository.save(targetUser);

        return ResponseEntity.ok(AuthDto.UserResponse.from(targetUser));
    }

    // Product Update Submission Management

    @Operation(summary = "제품 수정 요청 목록 조회", description = "승인 대기 중인 제품 수정 요청 목록을 조회합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.UpdateSubmissionResponse.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content)
    })
    @GetMapping("/product-updates")
    public ResponseEntity<List<ProductDto.UpdateSubmissionResponse>> getPendingProductUpdates(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        List<ProductDto.UpdateSubmissionResponse> submissions = updateSubmissionService.getPendingUpdateSubmissions();
        return ResponseEntity.ok(submissions);
    }

    @Operation(summary = "제품 수정 요청 승인", description = "대기 중인 제품 수정 요청을 승인합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승인 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.Response.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content),
            @ApiResponse(responseCode = "404", description = "수정 요청을 찾을 수 없음", content = @Content)
    })
    @PostMapping("/product-updates/{id}/approve")
    public ResponseEntity<ProductDto.Response> approveProductUpdate(
            @Parameter(description = "수정 요청 ID") @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        ProductDto.Response product = updateSubmissionService.approveUpdate(id, user);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "제품 수정 요청 거부", description = "대기 중인 제품 수정 요청을 거부합니다. (관리자 권한 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "거부 성공"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content),
            @ApiResponse(responseCode = "404", description = "수정 요청을 찾을 수 없음", content = @Content)
    })
    @PostMapping("/product-updates/{id}/reject")
    public ResponseEntity<Void> rejectProductUpdate(
            @Parameter(description = "수정 요청 ID") @PathVariable("id") Long id,
            @RequestBody(required = false) AdminDto.RejectRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        String reason = request != null ? request.getReason() : "승인 거부됨";
        updateSubmissionService.rejectUpdate(id, reason, user);
        return ResponseEntity.ok().build();
    }
}
