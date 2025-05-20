package com.example.oo_backend.book.controller;

import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;
import com.example.oo_backend.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
