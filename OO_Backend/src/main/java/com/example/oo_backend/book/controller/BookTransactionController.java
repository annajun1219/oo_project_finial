package com.example.oo_backend.book.controller;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.service.BookTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class BookTransactionController {

    private final BookTransactionService transactionService;

    @PostMapping("/direct")
    public ResponseEntity<BookPurchaseResponse> directPurchase(@RequestBody BookPurchaseRequest request) {
        BookPurchaseResponse response = transactionService.createDirectTransaction(request);
        return ResponseEntity.ok(response);
    }
}
