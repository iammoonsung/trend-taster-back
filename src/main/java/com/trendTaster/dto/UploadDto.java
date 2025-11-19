package com.trendTaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UploadDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResponse {
        private String token;
        private String filePath;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfirmRequest {
        private String token;
        private String publicUrl;
    }
}
