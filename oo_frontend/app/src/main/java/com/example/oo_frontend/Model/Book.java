package com.example.oo_frontend.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {

    // 응답 전용 필드
    @SerializedName("id")
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
    @SerializedName("bookid")
    private Long id;

    // 요청/응답 공통 필드
    @SerializedName("sellerId")
    private Long sellerId;


    @SerializedName("seller")
    private SellerInfo seller;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getAverageUsedPrice() { return averageUsedPrice; }
    public void setAverageUsedPrice(int averageUsedPrice) { this.averageUsedPrice = averageUsedPrice; }

    public int getDiscountRate() { return discountRate; }
    public void setDiscountRate(int discountRate) { this.discountRate = discountRate; }

    public boolean isMyPost() { return isMyPost; }
    public void setMyPost(boolean myPost) { isMyPost = myPost; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }
    public SellerInfo getSeller() {
        return seller;
    }

    public void setSeller(SellerInfo seller) {
        this.seller = seller;
    }

    public static class SellerInfo {
        @SerializedName("sellerId")
        private Long sellerId;

        @SerializedName("name")
        private String name;

        @SerializedName("phone")
        private String phone;

        @SerializedName("profileImage")
        private String profileImage;

        public Long getSellerId() { return sellerId; }
        public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getProfileImage() { return profileImage; }
        public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    }
}
