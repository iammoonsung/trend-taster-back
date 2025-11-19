package com.trendTaster.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stores",
    indexes = {
        @Index(name = "idx_store_name", columnList = "name"),
        @Index(name = "idx_store_status", columnList = "status")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_store_name", columnNames = "name")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 200)
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StoreStatus status = StoreStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(length = 500)
    private String rejectionReason;

    public enum StoreStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public void approve(User reviewer) {
        this.status = StoreStatus.APPROVED;
        this.reviewedBy = reviewer;
        this.rejectionReason = null;
    }

    public void reject(User reviewer, String reason) {
        this.status = StoreStatus.REJECTED;
        this.reviewedBy = reviewer;
        this.rejectionReason = reason;
    }
}
