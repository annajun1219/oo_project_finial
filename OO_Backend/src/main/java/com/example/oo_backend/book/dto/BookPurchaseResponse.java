package com.example.oo_backend.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookPurchaseResponse {
    private String message;
    private int originalPrice;
    private int averagePrice;
    private double discountRate;
}
