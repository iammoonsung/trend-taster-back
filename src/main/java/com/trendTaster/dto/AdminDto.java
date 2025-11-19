package com.trendTaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private Long totalUsers;
        private Long adminUsers;
    }
}
