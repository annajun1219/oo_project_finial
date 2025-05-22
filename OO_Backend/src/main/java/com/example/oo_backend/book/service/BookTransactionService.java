package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import java.util.List;

import java.util.UUID;

public interface BookTransactionService {
    BookPurchaseResponse createDirectTransaction(BookPurchaseRequest request);

    List<BookPurchaseResponse> getPurchaseHistory(Long userId, String status);
}
