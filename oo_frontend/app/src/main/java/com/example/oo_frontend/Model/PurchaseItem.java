package com.example.oo_frontend.Model;

public class PurchaseItem {
    public int bookId;         // 교재 ID
    public String title;       // 교재 제목
    public int price;          // 가격
    public String status;      // 상태 ("예약 중", "판매 완료" 등)
    public String createdAt;   // 거래 생성일
    public String imageUrl;    // 교재 이미지 URL
    public boolean hasReview;  // 후기 작성 여부

    public boolean isSelected = false;
}
