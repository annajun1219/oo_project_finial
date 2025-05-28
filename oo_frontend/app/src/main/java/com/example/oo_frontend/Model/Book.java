package com.example.oo_frontend.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Book implements Serializable {

    // 응답 전용 필드
    @SerializedName("productId")
    private Long productId;


    @SerializedName("status")
    private String status;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("averageUsedPrice")
    private int averageUsedPrice;

    @SerializedName("discountRate")
    private int discountRate;

    @SerializedName("isMyPost")
    private boolean isMyPost;

    // 요청/응답 공통 필드
    @SerializedName("sellerId")
    private Long sellerId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private int price;

    @SerializedName("officialPrice")
    private int officialPrice;

    @SerializedName("category")
    private String category;

    @SerializedName("professorName")
    private String professorName;

    public Book() {}

    // Getter & Setter
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getOfficialPrice() { return officialPrice; }
    public void setOfficialPrice(int officialPrice) { this.officialPrice = officialPrice; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getProfessorName() { return professorName; }
    public void setProfessorName(String professorName) { this.professorName = professorName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getAverageUsedPrice() { return averageUsedPrice; }
    public void setAverageUsedPrice(int averageUsedPrice) { this.averageUsedPrice = averageUsedPrice; }

    public int getDiscountRate() { return discountRate; }
    public void setDiscountRate(int discountRate) { this.discountRate = discountRate; }

    public boolean isMyPost() { return isMyPost; }
    public void setMyPost(boolean myPost) { isMyPost = myPost; }
}
