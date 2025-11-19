package com.trendTaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products",
    indexes = {
        @Index(name = "idx_store", columnList = "store"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_release_date", columnList = "releaseDate")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 50)
    private String store;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 50)
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
    private ProductStatus status = ProductStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @Column(nullable = false)
    @Builder.Default
    private Integer viewsCount = 0;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    public enum ProductStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public void approve() {
        this.status = ProductStatus.APPROVED;
    }

    public void reject() {
        this.status = ProductStatus.REJECTED;
    }

    public void incrementViews() {
        this.viewsCount++;
    }

    public void addImage(ProductImage image) {
        this.images.add(image);
        image.setProduct(this);
    }

    public boolean isNew() {
        if (releaseDate == null) return false;
        return releaseDate.isAfter(LocalDate.now().minusDays(7));
    }
}
