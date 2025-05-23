package com.example.oo_backend.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookPreviewDto {
    private Long id;
    private String title;
    private int price;
    private String professorName;
    private String category;
    private String imageUrl;
}