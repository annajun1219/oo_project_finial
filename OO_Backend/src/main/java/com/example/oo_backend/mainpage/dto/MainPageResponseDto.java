package com.example.oo_backend.mainpage.dto;

import com.example.oo_backend.book.dto.BookPreviewDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MainPageResponseDto {
    private String name;                        // 사용자 이름
    private List<String> categories;                // 단과대/분야 리스트
    private BookPreviewDto recommendation;          // 추천 도서 정보
}
