package com.example.oo_frontend.Model;

public class ReviewItem {
    // 요청용 필드
    public int reviewerId;
    public int productId;
    public int sellerId;
    public int transactionId;
    public int rating;
    public String content;

    // 응답용 필드
    public int reviewId;
    public String reviewerNickname;
    public String productName;
    public String createdAt;
    public String[] keywords;
}
