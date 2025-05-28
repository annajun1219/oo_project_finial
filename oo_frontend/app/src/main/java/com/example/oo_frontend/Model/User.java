package com.example.oo_frontend.Model;

public class User {
    private int userId;
    private String email;
    private String name;
    private String password;
    private String profileImageUrl;
    private float rating;

    // 기본 생성자 (Gson이나 Firebase 등에서 필요)
    public User() {
    }

    // 전체 필드를 초기화하는 생성자
    public User(int userId, String email, String name, String password, String profileImageUrl, float rating) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.rating = rating;
    }

    // Getter & Setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
