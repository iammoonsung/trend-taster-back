package com.trendTaster.repository;

import com.trendTaster.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.status = 'APPROVED' ORDER BY p.createdAt DESC")
    List<Product> findAllApprovedWithImages();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.status = 'PENDING' ORDER BY p.createdAt DESC")
    List<Product> findAllPending();

    @Query(value = "SELECT p FROM Product p WHERE p.status = 'PENDING' ORDER BY p.createdAt DESC",
           countQuery = "SELECT COUNT(p) FROM Product p WHERE p.status = 'PENDING'")
    Page<Product> findAllPendingPageable(Pageable pageable);

    Page<Product> findByStatusOrderByCreatedAtDesc(Product.ProductStatus status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
           "(:stores IS NULL OR p.store IN :stores) AND " +
           "(:categories IS NULL OR p.category IN :categories) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "p.status = 'APPROVED' " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findByFilters(
        @Param("stores") List<String> stores,
        @Param("categories") List<String> categories,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice,
        Pageable pageable
    );

    @Query("SELECT DISTINCT p.store FROM Product p WHERE p.status = 'APPROVED' ORDER BY p.store")
    List<String> findDistinctStores();

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.status = 'APPROVED' ORDER BY p.category")
    List<String> findDistinctCategories();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.status = 'PENDING'")
    Long countPendingProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.status = 'APPROVED'")
    Long countApprovedProducts();
}
