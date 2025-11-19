package com.trendTaster.repository;

import com.trendTaster.domain.UploadToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UploadTokenRepository extends JpaRepository<UploadToken, Long> {

    Optional<UploadToken> findByToken(String token);

    @Query("DELETE FROM UploadToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
