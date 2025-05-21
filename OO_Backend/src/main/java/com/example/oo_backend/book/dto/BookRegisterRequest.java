package com.example.oo_backend.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRegisterRequest {
    private Long sellerId;
    private String title;
    private String category;
    private String professorName;
    private int price;
    private String description;
    private String imageUrl;
}

