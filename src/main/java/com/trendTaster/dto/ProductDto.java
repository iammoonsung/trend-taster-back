package com.trendTaster.dto;

import com.trendTaster.domain.Product;
import com.trendTaster.domain.ProductImage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "제품명은 필수입니다")
        private String name;

        @NotBlank(message = "매장은 필수입니다")
        private String store;

        @NotNull(message = "가격은 필수입니다")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다")
        private Integer price;

        @NotBlank(message = "카테고리는 필수입니다")
        private String category;

        private String releaseDate;
        private String description;
        private String ingredients;
        private String barcode;
        private String location;

        @NotNull(message = "이미지는 최소 1개 이상 필요합니다")
        private List<String> imageUrls;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String store;
        private Integer price;
        private String category;
        private String releaseDate;
        private String description;
        private String ingredients;
        private String barcode;
        private String location;
        private String status;
        private String submittedBy;
        private Integer viewsCount;
        private List<ImageResponse> images;
        private Boolean isNew;
        private String createdAt;
        private String updatedAt;

        public static Response from(Product product) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return Response.builder()
                .id(product.getId())
                .name(product.getName())
                .store(product.getStore())
                .price(product.getPrice())
                .category(product.getCategory())
                .releaseDate(product.getReleaseDate() != null ? product.getReleaseDate().toString() : null)
                .description(product.getDescription())
                .ingredients(product.getIngredients())
                .barcode(product.getBarcode())
                .location(product.getLocation())
                .status(product.getStatus().name().toLowerCase())
                .submittedBy(product.getSubmittedBy() != null ? product.getSubmittedBy().getUsername() : null)
                .viewsCount(product.getViewsCount())
                .images(product.getImages().stream()
                    .map(ImageResponse::from)
                    .collect(Collectors.toList()))
                .isNew(product.isNew())
                .createdAt(product.getCreatedAt() != null ? product.getCreatedAt().format(formatter) : null)
                .updatedAt(product.getUpdatedAt() != null ? product.getUpdatedAt().format(formatter) : null)
                .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageResponse {
        private Long id;
        private String imageUrl;
        private Integer displayOrder;

        public static ImageResponse from(ProductImage image) {
            return ImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .displayOrder(image.getDisplayOrder())
                .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String name;
        private String store;
        private Integer price;
        private String category;
        private String releaseDate;
        private String description;
        private String ingredients;
        private String barcode;
        private String location;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FilterOptions {
        private List<String> stores;
        private List<String> categories;
    }
}
