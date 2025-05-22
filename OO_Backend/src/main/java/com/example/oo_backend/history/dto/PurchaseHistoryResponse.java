package com.example.oo_backend.history.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistoryResponse {
    private Long bookId;
    private String title;
    private double price;
    private String imageUrl;
    private String datetime;
    private String status; // "예약중", "구매완료" 등
}