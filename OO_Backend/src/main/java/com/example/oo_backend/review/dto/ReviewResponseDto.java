package com.example.oo_backend.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long reviewId;           // 리뷰 ID
    private String reviewerNickname; // 작성자 닉네임
    private int rating;              // 평점
    private String content;          // 리뷰 내용
    private LocalDate createdAt;     // 작성 날짜
}
