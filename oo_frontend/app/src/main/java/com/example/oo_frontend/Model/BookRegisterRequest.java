package com.example.oo_frontend.Model;

public class BookRegisterRequest {
    private String title;
    private String professorName;
    private int officialPrice;
    private int price;
    private String description;
    private String category;
    private long sellerId;
    private String imageUrl;

    // ✅ 생성자 생략 가능

    // ✅ Getter & Setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getProfessorName() { return professorName; }
    public void setProfessorName(String professorName) { this.professorName = professorName; }

    public int getOfficialPrice() { return officialPrice; }
    public void setOfficialPrice(int officialPrice) { this.officialPrice = officialPrice; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public long getSellerId() { return sellerId; }
    public void setSellerId(long sellerId) { this.sellerId = sellerId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}