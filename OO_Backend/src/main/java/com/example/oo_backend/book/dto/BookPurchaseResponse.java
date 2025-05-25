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
    private Long transactionId;
    private String productTitle;
    private String status;
    private Long sellerId;
    private Long buyerId;
    private ContactInfo contactInfo;
    private boolean hasReview;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactInfo {
        private String recipientName;
        private String recipientPhone;
    }

}
