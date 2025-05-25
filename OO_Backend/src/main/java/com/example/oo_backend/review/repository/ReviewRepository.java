package com.example.oo_backend.review.repository;

import com.example.oo_backend.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 거래 ID 기준으로 리뷰 존재 여부 확인 (중복 방지용)
    boolean existsByTransaction_Id(Long transactionId);

    // 특정 교재의 리a뷰 목록 조회
    List<Review> findByProductId(Long productId, Pageable pageable);

    // 특정 판매자의 리뷰 목록 조회
    List<Review> findBySellerId(Long sellerId, Pageable pageable);

}

