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
    private Integer officialPrice;       // null 허용
    private Integer averageUsedPrice;    // null 허용
    private Integer discountRate;        // null 허용
    private String description;
    private String imageUrl;
    private String status;
    private String createdAt;
    private SellerInfo seller;
    private Boolean isMyPost;            // null 허용

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SellerInfo {
        private Long sellerId;
        private String name;             // ✅ User.java에 존재하는 필드
        private String profileImage;     // ✅ User.java에 존재
        private String phone;            // ✅ User.java에 존재
    }
}
