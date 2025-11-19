package com.trendTaster.service;

import com.trendTaster.domain.Store;
import com.trendTaster.domain.User;
import com.trendTaster.dto.StoreDto;
import com.trendTaster.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;

    public List<StoreDto.Response> getAllApprovedStores() {
        List<Store> stores = storeRepository.findAllApproved();
        return stores.stream()
                .map(StoreDto.Response::from)
                .toList();
    }

    public List<StoreDto.Response> getPendingStores() {
        List<Store> stores = storeRepository.findAllPending();
        return stores.stream()
                .map(StoreDto.Response::from)
                .toList();
    }

    public Page<StoreDto.Response> getPendingStores(Pageable pageable) {
        Page<Store> stores = storeRepository.findAllPendingPageable(pageable);
        return stores.map(StoreDto.Response::from);
    }

    public StoreDto.Response getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다"));
        return StoreDto.Response.from(store);
    }

    @Transactional
    public StoreDto.Response createStore(StoreDto.CreateRequest request, User user) {
        // Check if store name already exists (any status)
        if (storeRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하거나 등록 대기 중인 매장/브랜드입니다");
        }

        Store store = Store.builder()
                .name(request.getName())
                .description(request.getDescription())
                .website(request.getWebsite())
                .status(Store.StoreStatus.PENDING)
                .submittedBy(user)
                .build();

        store = storeRepository.save(store);
        log.info("New store submission: {} by user: {}", store.getName(), user.getUsername());
        return StoreDto.Response.from(store);
    }

    @Transactional
    public StoreDto.Response updateStore(Long id, StoreDto.UpdateRequest request, User user) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다"));

        // Check ownership
        if (!store.getSubmittedBy().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("권한이 없습니다");
        }

        // Only allow updates if pending
        if (store.getStatus() != Store.StoreStatus.PENDING) {
            throw new IllegalArgumentException("승인 대기 중인 매장만 수정할 수 있습니다");
        }

        // Update fields - Note: Store entity needs setters or update methods
        // For now, create a new instance with updated values
        // You may want to add update methods to Store entity instead

        return StoreDto.Response.from(store);
    }

    @Transactional
    public void deleteStore(Long id, User user) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다"));

        // Check ownership
        if (!store.getSubmittedBy().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("권한이 없습니다");
        }

        storeRepository.delete(store);
        log.info("Store deleted: {} by user: {}", store.getName(), user.getUsername());
    }

    @Transactional
    public StoreDto.Response approveStore(Long id, User admin) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다"));

        if (store.getStatus() != Store.StoreStatus.PENDING) {
            throw new IllegalArgumentException("승인 대기 중인 매장만 승인할 수 있습니다");
        }

        store.approve(admin);
        log.info("Store approved: {} by admin: {}", store.getName(), admin.getUsername());
        return StoreDto.Response.from(store);
    }

    @Transactional
    public StoreDto.Response rejectStore(Long id, String reason, User admin) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다"));

        if (store.getStatus() != Store.StoreStatus.PENDING) {
            throw new IllegalArgumentException("승인 대기 중인 매장만 거부할 수 있습니다");
        }

        store.reject(admin, reason);
        log.info("Store rejected: {} by admin: {} - Reason: {}", store.getName(), admin.getUsername(), reason);
        return StoreDto.Response.from(store);
    }

    public boolean isStoreApproved(String storeName) {
        return storeRepository.findApprovedByName(storeName).isPresent();
    }
}
