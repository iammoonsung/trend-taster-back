package com.trendTaster.repository;

import com.trendTaster.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE s.status = 'APPROVED' ORDER BY s.name")
    List<Store> findAllApproved();

    @Query("SELECT s FROM Store s WHERE s.status = 'PENDING' ORDER BY s.createdAt DESC")
    List<Store> findAllPending();

    @Query(value = "SELECT s FROM Store s WHERE s.status = 'PENDING' ORDER BY s.createdAt DESC",
           countQuery = "SELECT COUNT(s) FROM Store s WHERE s.status = 'PENDING'")
    Page<Store> findAllPendingPageable(Pageable pageable);

    Optional<Store> findByName(String name);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.status = 'PENDING'")
    Long countPendingStores();

    @Query("SELECT COUNT(s) FROM Store s WHERE s.status = 'APPROVED'")
    Long countApprovedStores();

    boolean existsByNameAndStatus(String name, Store.StoreStatus status);

    @Query("SELECT s FROM Store s WHERE s.name = :name AND s.status = 'APPROVED'")
    Optional<Store> findApprovedByName(@Param("name") String name);
}
