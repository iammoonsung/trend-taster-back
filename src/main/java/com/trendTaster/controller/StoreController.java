package com.trendTaster.controller;

import com.trendTaster.domain.User;
import com.trendTaster.dto.StoreDto;
import com.trendTaster.service.StoreService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "매장/브랜드", description = "매장/브랜드 관리 API")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "승인된 매장/브랜드 목록 조회", description = "승인된 매장/브랜드 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<StoreDto.Response>> getAllApprovedStores() {
        List<StoreDto.Response> stores = storeService.getAllApprovedStores();
        return ResponseEntity.ok(stores);
    }

    @Operation(summary = "매장/브랜드 상세 조회", description = "매장/브랜드 ID로 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = StoreDto.Response.class))),
            @ApiResponse(responseCode = "404", description = "매장을 찾을 수 없음", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StoreDto.Response> getStore(
            @Parameter(description = "매장 ID") @PathVariable("id") Long id) {
        StoreDto.Response store = storeService.getStoreById(id);
        return ResponseEntity.ok(store);
    }

    @Operation(summary = "매장/브랜드 등록", description = "새로운 매장/브랜드를 등록합니다. (인증 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공",
                    content = @Content(schema = @Schema(implementation = StoreDto.Response.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @PostMapping
    public ResponseEntity<StoreDto.Response> createStore(
            @Valid @RequestBody StoreDto.CreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        StoreDto.Response store = storeService.createStore(request, user);
        return ResponseEntity.ok(store);
    }

    @Operation(summary = "매장/브랜드 수정", description = "매장/브랜드 정보를 수정합니다. (인증 필요, 본인이 등록한 매장만 가능)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = StoreDto.Response.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "매장을 찾을 수 없음", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<StoreDto.Response> updateStore(
            @Parameter(description = "매장 ID") @PathVariable("id") Long id,
            @Valid @RequestBody StoreDto.UpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        StoreDto.Response store = storeService.updateStore(id, request, user);
        return ResponseEntity.ok(store);
    }

    @Operation(summary = "매장/브랜드 삭제", description = "매장/브랜드를 삭제합니다. (인증 필요, 본인이 등록한 매장만 가능)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "매장을 찾을 수 없음", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(
            @Parameter(description = "매장 ID") @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        storeService.deleteStore(id, user);
        return ResponseEntity.ok().build();
    }
}
