package com.example.oo_backend.book.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;              // User의 userId 참조

    private String title;              // 교재명
    private String category;           // 카테고리
    private String professorName;      // 교수명
    private int price;                 // 가격
    private String description;        // 설명
    private String imageUrl;           // 이미지 경로 (선택)
    private String status;             // 판매 상태 (판매중, 예약중, 완료 등)

    private LocalDateTime createdAt;   // 등록일

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
