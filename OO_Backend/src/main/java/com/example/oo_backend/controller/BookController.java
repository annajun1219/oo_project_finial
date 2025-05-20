package com.example.oo_backend.controller;

import com.example.oo_backend.dto.BookRegisterRequest;
import com.example.oo_backend.dto.BookRegisterResponse;
import com.example.oo_backend.service.BookService;
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
