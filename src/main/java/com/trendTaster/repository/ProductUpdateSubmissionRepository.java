package com.trendTaster.repository;

import com.trendTaster.domain.ProductUpdateSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductUpdateSubmissionRepository extends JpaRepository<ProductUpdateSubmission, Long> {

    @Query("SELECT pus FROM ProductUpdateSubmission pus " +
           "LEFT JOIN FETCH pus.product " +
           "LEFT JOIN FETCH pus.submittedBy " +
           "WHERE pus.status = 'PENDING' " +
           "ORDER BY pus.createdAt DESC")
    List<ProductUpdateSubmission> findPendingSubmissions();

    Page<ProductUpdateSubmission> findByStatusOrderByCreatedAtDesc(
        ProductUpdateSubmission.SubmissionStatus status,
        Pageable pageable
    );

    @Query("SELECT COUNT(pus) FROM ProductUpdateSubmission pus WHERE pus.status = 'PENDING'")
    long countPending();
}
