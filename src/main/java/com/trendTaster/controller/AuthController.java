package com.trendTaster.controller;

import com.trendTaster.domain.User;
import com.trendTaster.dto.AuthDto;
import com.trendTaster.service.AuthService;
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

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = AuthDto.AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일/사용자명 중복 등)", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthDto.AuthResponse> register(@Valid @RequestBody AuthDto.RegisterRequest request) {
        log.info("Register request for email: {}", request.getEmail());
        AuthDto.AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = AuthDto.AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (이메일 또는 비밀번호 오류)", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthDto.AuthResponse> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        AuthDto.AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "현재 사용자 정보 조회", description = "JWT 토큰으로 현재 로그인한 사용자의 정보를 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AuthDto.UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요 (토큰 없음 또는 만료)", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<AuthDto.UserResponse> getCurrentUser(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        AuthDto.UserResponse response = authService.getCurrentUser(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 (클라이언트에서 토큰 삭제 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // JWT는 stateless이므로 서버에서 할 작업 없음
        // 클라이언트에서 토큰 삭제 처리
        Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "로그아웃되었습니다. 클라이언트에서 토큰을 삭제해주세요.");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
