package com.trendTaster.controller;

import com.trendTaster.domain.User;
import com.trendTaster.dto.ProductDto;
import com.trendTaster.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "제품", description = "제품 관리 API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "제품 목록 조회", description = "필터링 조건에 따라 제품 목록을 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProductDto.Response>> getProducts(
            @Parameter(description = "편의점/브랜드 필터 (복수 선택 가능)") @RequestParam(value = "stores", required = false) List<String> stores,
            @Parameter(description = "카테고리 필터 (복수 선택 가능)") @RequestParam(value = "categories", required = false) List<String> categories,
            @Parameter(description = "최소 가격") @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @Parameter(description = "최대 가격") @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto.Response> products = productService.getProducts(stores, categories, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "필터 옵션 조회", description = "DB에 실제로 존재하는 매장과 카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/filters")
    public ResponseEntity<ProductDto.FilterOptions> getFilterOptions() {
        ProductDto.FilterOptions filterOptions = productService.getFilterOptions();
        return ResponseEntity.ok(filterOptions);
    }

    @Operation(summary = "제품 상세 조회", description = "제품 ID로 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.Response.class))),
            @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.Response> getProduct(
            @Parameter(description = "제품 ID") @PathVariable("id") Long id) {
        ProductDto.Response product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "제품 등록", description = "새로운 제품을 등록합니다. (인증 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.Response.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductDto.Response> createProduct(
            @Valid @RequestBody ProductDto.CreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        ProductDto.Response product = productService.createProduct(request, user);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "제품 수정", description = "제품 정보를 수정합니다. (인증 필요, 본인이 등록한 제품만 가능)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = ProductDto.Response.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto.Response> updateProduct(
            @Parameter(description = "제품 ID") @PathVariable("id") Long id,
            @Valid @RequestBody ProductDto.UpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        ProductDto.Response product = productService.updateProduct(id, request, user);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "제품 삭제", description = "제품을 삭제합니다. (인증 필요, 본인이 등록한 제품만 가능)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "제품 ID") @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        productService.deleteProduct(id, user);
        return ResponseEntity.ok().build();
    }
}
