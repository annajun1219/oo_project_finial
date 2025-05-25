package com.example.oo_backend.book.controller;

import com.example.oo_backend.book.dto.BookPreviewDto;
import com.example.oo_backend.book.dto.BookDetailResponse;
import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;
import com.example.oo_backend.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @PostMapping("/register")
    public ResponseEntity<BookRegisterResponse> registerBook(@RequestBody BookRegisterRequest request) {
        BookRegisterResponse response = bookService.registerBook(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<BookDetailResponse> getBookDetail(
            @PathVariable Long productId,
            @RequestHeader(value = "viewerId", required = false) Long viewerId) {

        BookDetailResponse response = bookService.getBookDetail(productId, viewerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-department")
    public ResponseEntity<List<BookPreviewDto>> getBooksByDepartment(@RequestParam String department) {
        return ResponseEntity.ok(bookService.getBooksByDepartment(department));
    }

    @GetMapping("/search-by-professor")
    public ResponseEntity<List<BookPreviewDto>> searchByProfessor(@RequestParam String keyword, @RequestParam String category) {
        return ResponseEntity.ok(bookService.searchByProfessorAndCategory(keyword, category));
    }

    @GetMapping("/search-by-title")
    public ResponseEntity<List<BookPreviewDto>> searchByTitle(@RequestParam String keyword, @RequestParam String category) {
        return ResponseEntity.ok(bookService.searchByTitleAndCategory(keyword, category));
    }
}