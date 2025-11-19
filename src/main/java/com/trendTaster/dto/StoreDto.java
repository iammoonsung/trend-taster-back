package com.trendTaster.dto;

import com.trendTaster.domain.Store;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.format.DateTimeFormatter;

public class StoreDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "매장/브랜드명은 필수입니다")
        private String name;

        private String description;
        private String website;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private String website;
        private String status;
        private String submittedBy;
        private String reviewedBy;
        private String rejectionReason;
        private String createdAt;
        private String updatedAt;

        public static Response from(Store store) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return Response.builder()
                .id(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .website(store.getWebsite())
                .status(store.getStatus().name().toLowerCase())
                .submittedBy(store.getSubmittedBy() != null ? store.getSubmittedBy().getUsername() : null)
                .reviewedBy(store.getReviewedBy() != null ? store.getReviewedBy().getUsername() : null)
                .rejectionReason(store.getRejectionReason())
                .createdAt(store.getCreatedAt() != null ? store.getCreatedAt().format(formatter) : null)
                .updatedAt(store.getUpdatedAt() != null ? store.getUpdatedAt().format(formatter) : null)
                .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String description;
        private String website;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectRequest {
        @NotBlank(message = "거부 사유는 필수입니다")
        private String reason;
    }
}
