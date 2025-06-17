package com.example.oo_frontend.Model;

public class FavoriteItem {
    public int bookId;
    public String title;
    public int price; // ✅ 추가
    public String imageUrl;
    public String status;
    public String createdAt; // ✅ 추가

    // 생성자
    public FavoriteItem(int bookId, String title, int price, String imageUrl, String status, String createdAt) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    // 기본 생성자 (Gson 등 파싱용)
    public FavoriteItem() {
    }
}