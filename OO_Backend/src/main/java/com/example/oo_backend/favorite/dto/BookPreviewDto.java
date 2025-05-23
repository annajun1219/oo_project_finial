package com.example.oo_backend.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookPreviewDto {

    private Long bookId;
    private String title;
    private int price;
    private String imageUrl;
    private String status;
    private LocalDate createdAt;
}
