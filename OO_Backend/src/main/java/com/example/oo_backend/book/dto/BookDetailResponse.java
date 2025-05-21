package com.example.oo_backend.book.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetailResponse {

    private Long productId;
    private String title;
    private int price;
    private Integer officialPrice;       // 응답 유동성 반영: null 허용
    private Integer averageUsedPrice;    // 응답 유동성 반영: null 허용
    private Integer discountRate;        // 응답 유동성 반영: null 허용
    private String description;
    private String imageUrl;
    private String status;
    private String createdAt;
    private SellerInfo seller;
    private Boolean isMyPost;            // 응답 유동성 반영: null 허용

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SellerInfo {
        private Long sellerId;
        private String nickname;
        private String profileImage;
        private Integer warningCount;    // 필요 시 추가 필드
    }
}
