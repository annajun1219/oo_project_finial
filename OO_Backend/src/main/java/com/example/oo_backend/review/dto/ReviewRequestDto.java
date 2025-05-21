package com.example.oo_backend.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private Long reviewerId;      // 리뷰 작성자 ID
    private Long productId;       // 리뷰 대상 교재 ID
    private Long sellerId;        // 판매자 ID (선택)
    private Long transactionId;   // 거래 ID (중복 리뷰 방지용, 추천)
    private int rating;           // 평점 (1~5)
    private String content;       // 리뷰 내용
}
