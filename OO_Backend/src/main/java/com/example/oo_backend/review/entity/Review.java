package com.example.oo_backend.review.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reviewerId;      // 리뷰 작성자
    private Long productId;       // 리뷰 대상 교재
    private Long sellerId;        // 판매자 (optional)
    private Long transactionId;   // 거래 ID (optional)

    private int rating;           // 평점 (1~5)
    private String content;       // 리뷰 내용

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
