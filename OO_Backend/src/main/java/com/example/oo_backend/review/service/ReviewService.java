package com.example.oo_backend.review.service;

import com.example.oo_backend.review.dto.ReviewRequestDto;
import com.example.oo_backend.review.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {

    // 리뷰 작성
    ReviewResponseDto createReview(ReviewRequestDto dto);

    // 리뷰 조회 (productId 또는 sellerId 기준)
    List<ReviewResponseDto> getReviews(Long productId, Long sellerId, String sortBy, int offset, int limit);
}
