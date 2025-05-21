package com.example.oo_backend.review.service;

import com.example.oo_backend.review.dto.ReviewRequestDto;
import com.example.oo_backend.review.dto.ReviewResponseDto;
import com.example.oo_backend.review.entity.Review;
import com.example.oo_backend.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResponseDto createReview(ReviewRequestDto dto) {
        // 평점 유효성 검증
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("평점은 1점 이상 5점 이하만 가능합니다.");
        }

        // 중복 리뷰 확인
        if (dto.getTransactionId() != null && reviewRepository.existsByTransactionId(dto.getTransactionId())) {
            throw new IllegalStateException("이미 이 거래에 대해 리뷰가 작성되었습니다.");
        }

        // 리뷰 엔티티 생성
        Review review = Review.builder()
                .reviewerId(dto.getReviewerId())
                .productId(dto.getProductId())
                .sellerId(dto.getSellerId())
                .transactionId(dto.getTransactionId())
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        Review saved = reviewRepository.save(review);

        return ReviewResponseDto.builder()
                .reviewId(saved.getId())
                .reviewerNickname("익명")  // 추후 유저 정보 조인 시 수정
                .rating(saved.getRating())
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt().toLocalDate())
                .build();
    }

    @Override
    public List<ReviewResponseDto> getReviews(Long productId, Long sellerId, String sortBy, int offset, int limit) {
        // 정렬 조건 처리
        Sort sort = sortBy.equals("rating") ?
                Sort.by(Sort.Direction.DESC, "rating") :
                Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(offset / limit, limit, sort);

        List<Review> reviews;

        if (productId != null) {
            reviews = reviewRepository.findByProductId(productId, pageable);
        } else if (sellerId != null) {
            reviews = reviewRepository.findBySellerId(sellerId, pageable);
        } else {
            throw new IllegalArgumentException("productId 또는 sellerId 중 하나는 반드시 전달되어야 합니다.");
        }

        return reviews.stream().map(r -> ReviewResponseDto.builder()
                .reviewId(r.getId())
                .reviewerNickname("익명")  // 추후 닉네임 로직 추가
                .rating(r.getRating())
                .content(r.getContent())
                .createdAt(r.getCreatedAt().toLocalDate())
                .build()
        ).collect(Collectors.toList());
    }
}
