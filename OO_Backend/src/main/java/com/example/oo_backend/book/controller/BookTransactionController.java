package com.example.oo_backend.book.controller;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.service.BookTransactionService;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class BookTransactionController {

    private final BookTransactionService transactionService;
    private final UserRepository userRepository;

    @PostMapping("/direct")
    public ResponseEntity<BookPurchaseResponse> directPurchase(@RequestBody BookPurchaseRequest request) {
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (!buyer.getName().equals(request.getRecipientName())) {
            throw new IllegalArgumentException("구매자 이름과 수령인 이름이 일치하지 않습니다.");
        }

        BookPurchaseResponse response = transactionService.createDirectTransaction(request);
        return ResponseEntity.ok(response);
    }
}
