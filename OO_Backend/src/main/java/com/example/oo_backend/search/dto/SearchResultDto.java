package com.example.oo_backend.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SearchResultDto {
    private Long id;
    private String title;
    private String professorName;
    private String category;
    private int price;
    private String imageUrl;
}