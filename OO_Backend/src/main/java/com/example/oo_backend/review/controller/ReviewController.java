package com.example.oo_backend.review.controller;

import com.example.oo_backend.review.dto.ReviewRequestDto;
import com.example.oo_backend.review.dto.ReviewResponseDto;
import com.example.oo_backend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 📤 리뷰 작성
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto response = reviewService.createReview(requestDto);
        return ResponseEntity.ok(Map.of(
                "message", "리뷰가 성공적으로 등록되었습니다.",
                "reviewId", response.getReviewId()
        ));
    }

    // 📥 리뷰 조회
    @GetMapping
    public ResponseEntity<?> getReviews(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(defaultValue = "latest") String sortBy,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<ReviewResponseDto> reviews = reviewService.getReviews(productId, sellerId, sortBy, offset, limit);
        return ResponseEntity.ok(Map.of("reviews", reviews));
    }
}
