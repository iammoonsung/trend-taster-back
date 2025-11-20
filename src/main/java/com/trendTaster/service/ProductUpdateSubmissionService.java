package com.trendTaster.service;

import com.trendTaster.domain.Product;
import com.trendTaster.domain.ProductUpdateSubmission;
import com.trendTaster.domain.User;
import com.trendTaster.dto.ProductDto;
import com.trendTaster.repository.ProductRepository;
import com.trendTaster.repository.ProductUpdateSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductUpdateSubmissionService {

    private final ProductUpdateSubmissionRepository updateSubmissionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ProductDto.UpdateSubmissionResponse submitUpdate(Long productId, ProductDto.UpdateRequest request, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다"));

        // Check ownership or admin
        if (!product.getSubmittedBy().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("수정 권한이 없습니다");
        }

        // Create update submission
        ProductUpdateSubmission submission = ProductUpdateSubmission.builder()
                .product(product)
                .name(request.getName())
                .store(request.getStore())
                .price(request.getPrice())
                .category(request.getCategory())
                .releaseDate(request.getReleaseDate() != null ? LocalDate.parse(request.getReleaseDate()) : null)
                .description(request.getDescription())
                .ingredients(request.getIngredients())
                .barcode(request.getBarcode())
                .location(request.getLocation())
                .status(ProductUpdateSubmission.SubmissionStatus.PENDING)
                .submittedBy(user)
                .build();

        submission = updateSubmissionRepository.save(submission);
        log.info("Product update submission created: {} for product: {}", submission.getId(), productId);

        return ProductDto.UpdateSubmissionResponse.from(submission);
    }

    public List<ProductDto.UpdateSubmissionResponse> getPendingUpdateSubmissions() {
        List<ProductUpdateSubmission> submissions = updateSubmissionRepository.findPendingSubmissions();
        return submissions.stream()
                .map(ProductDto.UpdateSubmissionResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto.Response approveUpdate(Long submissionId, User reviewer) {
        ProductUpdateSubmission submission = updateSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("업데이트 요청을 찾을 수 없습니다"));

        if (!submission.isPending()) {
            throw new IllegalArgumentException("이미 처리된 요청입니다");
        }

        Product product = submission.getProduct();

        // Apply changes to product using the updateDetails method
        product.updateDetails(
                submission.getName(),
                submission.getStore(),
                submission.getPrice(),
                submission.getCategory(),
                submission.getReleaseDate(),
                submission.getDescription(),
                submission.getIngredients(),
                submission.getBarcode(),
                submission.getLocation()
        );

        productRepository.save(product);
        submission.approve(reviewer);
        updateSubmissionRepository.save(submission);

        log.info("Product update approved: {} by admin: {}", submissionId, reviewer.getEmail());

        return ProductDto.Response.from(product);
    }

    @Transactional
    public void rejectUpdate(Long submissionId, String reason, User reviewer) {
        ProductUpdateSubmission submission = updateSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("업데이트 요청을 찾을 수 없습니다"));

        if (!submission.isPending()) {
            throw new IllegalArgumentException("이미 처리된 요청입니다");
        }

        submission.reject(reviewer, reason);
        updateSubmissionRepository.save(submission);

        log.info("Product update rejected: {} by admin: {}", submissionId, reviewer.getEmail());
    }

    public long countPendingUpdateSubmissions() {
        return updateSubmissionRepository.countPending();
    }
}
