package com.example.oo_backend.book.controller;

import com.example.oo_backend.book.dto.BookPreviewDto;
import com.example.oo_backend.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final BookService bookService;

    // 1. 시간표 기반 (과목 + 교수)
    @GetMapping("/by-schedule")
    public ResponseEntity<List<BookPreviewDto>> recommendBySchedule(@RequestParam Long userId) {
        List<BookPreviewDto> recommendedBooks = bookService.getRecommendedBooksBySchedule(userId);
        return ResponseEntity.ok(recommendedBooks);
    }

    // 2. 과목 기반 추천
    @GetMapping("/by-subject")
    public ResponseEntity<List<BookPreviewDto>> recommendBySubject(@RequestParam Long userId) {
        List<BookPreviewDto> books = bookService.getBooksBySubjectFromSchedule(userId);
        return ResponseEntity.ok(books);
    }

    // 3. 교수 기반 추천
    @GetMapping("/by-professor")
    public ResponseEntity<List<BookPreviewDto>> recommendByProfessor(@RequestParam Long userId) {
        List<BookPreviewDto> books = bookService.getBooksByProfessorFromSchedule(userId);
        return ResponseEntity.ok(books);
    }
}