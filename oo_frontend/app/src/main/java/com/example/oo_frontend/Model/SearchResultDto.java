package com.example.oo_frontend.Model;

public class SearchResultDto {
    private Long productId;
    private String title;
    private String professorName;
    private String imageUrl;

    public SearchResultDto() {}

    public SearchResultDto(Long productId, String title, String professorName, String imageUrl) {
        this.productId = productId;
        this.title = title;
        this.professorName = professorName;
        this.imageUrl = imageUrl;
    }

    // Getter & Setter
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 🔁 SearchResultDto → Book 변환
    public Book toBook() {
        Book book = new Book();
        book.setProductId(this.productId);
        book.setTitle(this.title);
        book.setProfessorName(this.professorName);
        book.setImageUrl(this.imageUrl);
        return book;
    }
}