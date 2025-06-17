package com.example.oo_frontend.Model;

public class SaleItem {
    public long transactionId;
    public int bookId;
    public String title;
    public int price;
    public String imageUrl;
    public String status;
    public String createdAt;
    public boolean isSelected = false;

    public SaleItem(long transactionId, int bookId, String title, int price, String imageUrl, String status, String createdAt) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    // 기존 생성자 사용 시 임시용
    public SaleItem(String title, String priceText, String createdAt, String status, String imageUrl) {
        this.title = title;
        this.price = 0; // 실제 price는 서버에서 받음
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }
}
