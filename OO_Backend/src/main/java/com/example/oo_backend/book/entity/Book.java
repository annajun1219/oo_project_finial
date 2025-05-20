package com.example.oo_backend.book.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String title;         // 교재명
    private String category;      // 카테고리
    private String professorName; // 교수명
    private int price;            // 가격
    private String description;   // 설명

    private String imageUrl;      // 이미지 경로 (선택)
}
