package com.example.oo_backend.history.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 구매자 식별
    private Long bookId;
    private String title;
    private double price;
    private String imageUrl;
    private LocalDateTime datetime;
    private String status; // 예: "예약중", "구매완료"
}
