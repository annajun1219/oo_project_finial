package com.example.oo_frontend.Model;

public class CategoryItem {
    private String name;
    private int imageResId;
    private String type;  // ✅ 추가: 전공 or 기타 구분용

    public CategoryItem(String name, int imageResId, String type) {
        this.name = name;
        this.imageResId = imageResId;
        this.type = type;
    }

    // 전공 카테고리 등 기존 코드 호환을 위한 생성자도 유지
    public CategoryItem(String name, int imageResId) {
        this(name, imageResId, "major");  // 기본값을 "major"로 설정
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getType() {
        return type;
    }
}
