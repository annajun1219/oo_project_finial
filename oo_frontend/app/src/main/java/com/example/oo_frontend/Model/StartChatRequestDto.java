package com.example.oo_frontend.Model;

public class StartChatRequestDto {
    private Long buyerId;
    private Long sellerId;
    private Long bookId;

    public StartChatRequestDto(Long buyerId, Long sellerId, Long bookId) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.bookId = bookId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Long getBookId() {
        return bookId;
    }
}
