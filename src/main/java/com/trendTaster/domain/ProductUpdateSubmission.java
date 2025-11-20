package com.trendTaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "product_update_submissions",
    indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_submitted_by", columnList = "submitted_by")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductUpdateSubmission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Proposed changes (nullable means no change)
    @Column(length = 200)
    private String name;

    @Column(length = 50)
    private String store;

    @Column
    private Integer price;

    @Column(length = 50)
    private String category;

    @Column
    private LocalDate releaseDate;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String ingredients;

    @Column(length = 50)
    private String barcode;

    @Column(length = 200)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SubmissionStatus status = SubmissionStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by", nullable = false)
    private User submittedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(length = 500)
    private String rejectionReason;

    public enum SubmissionStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public void approve(User reviewer) {
        this.status = SubmissionStatus.APPROVED;
        this.reviewedBy = reviewer;
    }

    public void reject(User reviewer, String reason) {
        this.status = SubmissionStatus.REJECTED;
        this.reviewedBy = reviewer;
        this.rejectionReason = reason;
    }

    public boolean isPending() {
        return this.status == SubmissionStatus.PENDING;
    }
}
