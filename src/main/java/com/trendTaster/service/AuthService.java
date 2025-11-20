package com.trendTaster.service;

import com.trendTaster.domain.User;
import com.trendTaster.dto.AuthDto;
import com.trendTaster.repository.UserRepository;
import com.trendTaster.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다");
        }

        // Create user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.UserRole.USER)
                .build();

        user = userRepository.save(user);

        // Generate token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());

        return AuthDto.AuthResponse.builder()
                .token(token)
                .user(AuthDto.UserResponse.from(user))
                .build();
    }

    @Transactional
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다");
        }

        // Generate token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());

        return AuthDto.AuthResponse.builder()
                .token(token)
                .user(AuthDto.UserResponse.from(user))
                .build();
    }

    public AuthDto.UserResponse getCurrentUser(User user) {
        return AuthDto.UserResponse.from(user);
    }

    @Transactional
    public AuthDto.UserResponse updateProfile(User user, AuthDto.UpdateProfileRequest request) {
        // If username is provided and different from current username
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            // Check if the new username is already taken by another user
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("이미 사용 중인 사용자명입니다");
            }
            user.updateUsername(request.getUsername());
            userRepository.save(user);
            log.info("User {} updated username to: {}", user.getEmail(), request.getUsername());
        }

        return AuthDto.UserResponse.from(user);
    }
}
