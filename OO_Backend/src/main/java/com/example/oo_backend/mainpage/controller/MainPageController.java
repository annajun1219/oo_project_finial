package com.example.oo_backend.mainpage.controller;

import com.example.oo_backend.book.dto.BookPreviewDto;
import com.example.oo_backend.book.service.BookService;
import com.example.oo_backend.mainpage.dto.MainPageResponseDto;
import com.example.oo_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainPageController {

    private final UserService userService;
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<MainPageResponseDto> getMainPage(@RequestParam Long userId) {

        // 1. 사용자 이름
        String name = userService.getUserName(userId);

        // 2. 카테고리 리스트
        List<String> categories = List.of(
                "문과대학", "이과대학", "공과대학", "생활과학대학", "사회과학대학",
                "법과대학", "경상대학", "음악대학", "약학대학", "미술대학",
                "교양서적", "자격증"
        );

        // 3. 추천 교재
        List<BookPreviewDto> recommendedBooks = bookService.getRecommendedBooksBySchedule(userId);
        BookPreviewDto recommendation = recommendedBooks.isEmpty() ? null : recommendedBooks.get(0);

        return ResponseEntity.ok(MainPageResponseDto.builder()
                .name(name)
                .categories(categories)
                .recommendation(recommendation)
                .build());
    }
}