package com.trendTaster.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users",
    indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_username", columnList = "username")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private UserRole role = UserRole.USER;

    public enum UserRole {
        USER,
        ADMIN,
        SUPER_ADMIN
    }

    public void promoteToAdmin() {
        this.role = UserRole.ADMIN;
    }

    public void promoteToSuperAdmin() {
        this.role = UserRole.SUPER_ADMIN;
    }

    public void demoteToUser() {
        this.role = UserRole.USER;
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN || this.role == UserRole.SUPER_ADMIN;
    }

    public boolean isSuperAdmin() {
        return this.role == UserRole.SUPER_ADMIN;
    }
}
