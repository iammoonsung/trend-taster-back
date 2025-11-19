package com.trendTaster.controller;

import com.trendTaster.domain.UploadToken;
import com.trendTaster.domain.User;
import com.trendTaster.dto.UploadDto;
import com.trendTaster.repository.UploadTokenRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "업로드", description = "파일 업로드 관련 API (토큰 기반 보안)")
public class UploadController {

    private final UploadTokenRepository uploadTokenRepository;

    // Supabase Storage URL 패턴 (환경 변수로 설정 가능)
    private static final String SUPABASE_URL_PATTERN =
            "^https://[a-z0-9]+\\.supabase\\.co/storage/v1/object/public/product-images/.*\\.(jpg|jpeg|png|webp)$";

    @Operation(summary = "업로드 토큰 발급", description = "이미지 업로드를 위한 임시 토큰을 발급합니다. (인증 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공",
                    content = @Content(schema = @Schema(implementation = UploadDto.TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @PostMapping("/token")
    public ResponseEntity<UploadDto.TokenResponse> getUploadToken(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // Generate unique token
        String token = UUID.randomUUID().toString();

        // Generate unique file path
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        String filePath = "product-images/" + timestamp + "-" + randomString + ".jpg";

        // Create upload token (expires in 10 minutes)
        UploadToken uploadToken = UploadToken.builder()
                .token(token)
                .filePath(filePath)
                .publicUrl("") // Will be set on confirm
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        uploadTokenRepository.save(uploadToken);

        log.debug("Upload token generated for user {}: {}", user.getId(), token);

        return ResponseEntity.ok(UploadDto.TokenResponse.builder()
                .token(token)
                .filePath(filePath)
                .build());
    }

    @Operation(summary = "업로드 확인", description = "업로드 완료 후 토큰을 검증하고 URL을 저장합니다. (인증 필요)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업로드 확인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 토큰", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    })
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmUpload(
            @RequestBody UploadDto.ConfirmRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // Find token
        UploadToken uploadToken = uploadTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다"));

        // Validate token
        if (!uploadToken.isValid()) {
            throw new IllegalArgumentException("만료되었거나 이미 사용된 토큰입니다");
        }

        // Validate user
        if (!uploadToken.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("권한이 없는 토큰입니다");
        }

        // Validate URL format (must be from Supabase Storage)
        if (!Pattern.matches(SUPABASE_URL_PATTERN, request.getPublicUrl())) {
            throw new IllegalArgumentException("유효하지 않은 이미지 URL입니다. Supabase Storage URL만 허용됩니다.");
        }

        // Mark token as used and save URL
        uploadToken.markAsUsed();
        uploadToken.setPublicUrl(request.getPublicUrl());
        uploadTokenRepository.save(uploadToken);

        log.info("Upload confirmed for user {}: {}", user.getId(), request.getPublicUrl());

        return ResponseEntity.ok().build();
    }
}
