package com.trendTaster.dto;

import lombok.*;

public class AdminDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stats {
        private Long pendingSubmissions;
        private Long approvedProducts;
        private Long pendingStores;
        private Long approvedStores;
        private Long pendingUpdateSubmissions;
        private Long totalUsers;
        private Long adminUsers;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectRequest {
        private String reason;
    }
}
