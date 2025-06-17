package com.example.oo_frontend.Model;

import com.google.gson.annotations.SerializedName;

public class BookRegisterResponse {
    @SerializedName("bookId")
    private Long bookId;

    @SerializedName("message")
    private String message;

    public Long getBookId() {
        return bookId;
    }

    public String getMessage() {
        return message;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}