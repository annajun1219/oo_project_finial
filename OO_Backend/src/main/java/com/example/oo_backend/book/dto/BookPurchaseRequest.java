package com.example.oo_backend.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookPurchaseRequest {
    private Long buyerId;
    private Long productId;
    private int price;
    private String paymentMethod;
    private String recipientName;
    private String recipientPhone;

}
