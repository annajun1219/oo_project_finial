package com.example.oo_backend.book.controller;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.service.BookTransactionService;
import com.example.oo_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class BookTransactionController {

    private final BookTransactionService transactionService;
    private final UserService userService;

    @PostMapping("/direct")
    public ResponseEntity<BookPurchaseResponse> directPurchase(@RequestBody BookPurchaseRequest request,
                                                               Principal principal) {
        UUID buyerId = userService.getUserIdFromEmail(principal.getName());
        BookPurchaseResponse response = transactionService.createDirectTransaction(buyerId, request);
        return ResponseEntity.ok(response);
    }
}
