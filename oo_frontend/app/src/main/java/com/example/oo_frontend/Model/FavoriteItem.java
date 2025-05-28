package com.example.oo_frontend.Model;

public class FavoriteItem {
    public int bookId;
    public String title;
    public String imageUrl;
    public String status;

    public FavoriteItem(int bookId, String title, String imageUrl, String status) {
        this.bookId = bookId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.status = status;
    }
}
