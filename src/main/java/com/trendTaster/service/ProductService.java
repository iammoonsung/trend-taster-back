package com.trendTaster.service;

import com.trendTaster.domain.Product;
import com.trendTaster.domain.ProductImage;
import com.trendTaster.domain.User;
import com.trendTaster.dto.ProductDto;
import com.trendTaster.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreService storeService;

    public Page<ProductDto.Response> getProducts(
            List<String> stores,
            List<String> categories,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    ) {
        Page<Product> products = productRepository.findByFilters(stores, categories, minPrice, maxPrice, pageable);
        return products.map(ProductDto.Response::from);
    }

    @Transactional
    public ProductDto.Response getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다"));

        product.incrementViews();
        return ProductDto.Response.from(product);
    }

    @Transactional
    public ProductDto.Response createProduct(ProductDto.CreateRequest request, User user) {
        // Validate that store is approved
        if (!storeService.isStoreApproved(request.getStore())) {
            throw new IllegalArgumentException("등록되지 않은 매장/브랜드입니다. 먼저 매장/브랜드를 추가해주세요.");
        }

        Product product = Product.builder()
                .name(request.getName())
                .store(request.getStore())
                .price(request.getPrice())
                .category(request.getCategory())
                .releaseDate(request.getReleaseDate() != null ? LocalDate.parse(request.getReleaseDate()) : null)
                .description(request.getDescription())
                .ingredients(request.getIngredients())
                .barcode(request.getBarcode())
                .location(request.getLocation())
                .status(Product.ProductStatus.PENDING)
                .submittedBy(user)
                .viewsCount(0)
                .build();

        // Add images
        for (int i = 0; i < request.getImageUrls().size(); i++) {
            ProductImage image = ProductImage.builder()
                    .imageUrl(request.getImageUrls().get(i))
                    .displayOrder(i)
                    .build();
            product.addImage(image);
        }

        product = productRepository.save(product);
        return ProductDto.Response.from(product);
    }

    @Transactional
    public ProductDto.Response updateProduct(Long id, ProductDto.UpdateRequest request, User user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다"));

        // Check ownership
        if (!product.getSubmittedBy().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("권한이 없습니다");
        }

        // Update fields if provided
        // Note: In a real application, you'd use a builder or update methods
        // For now, this is a placeholder - you'll need to add setters to Product entity

        return ProductDto.Response.from(product);
    }

    @Transactional
    public void deleteProduct(Long id, User user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다"));

        // Check ownership
        if (!product.getSubmittedBy().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("권한이 없습니다");
        }

        productRepository.delete(product);
    }

    public List<ProductDto.Response> getPendingSubmissions() {
        List<Product> products = productRepository.findAllPending();
        return products.stream()
                .map(ProductDto.Response::from)
                .toList();
    }

    public Page<ProductDto.Response> getPendingSubmissions(Pageable pageable) {
        Page<Product> products = productRepository.findAllPendingPageable(pageable);
        return products.map(ProductDto.Response::from);
    }

    @Transactional
    public ProductDto.Response approveProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다"));

        product.approve();
        return ProductDto.Response.from(product);
    }

    @Transactional
    public void rejectProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다"));

        product.reject();
    }

    public ProductDto.FilterOptions getFilterOptions() {
        // Get approved stores from Store table
        List<String> stores = storeService.getAllApprovedStores()
                .stream()
                .map(storeDto -> storeDto.getName())
                .toList();
        List<String> categories = productRepository.findDistinctCategories();

        return ProductDto.FilterOptions.builder()
                .stores(stores)
                .categories(categories)
                .build();
    }
}
