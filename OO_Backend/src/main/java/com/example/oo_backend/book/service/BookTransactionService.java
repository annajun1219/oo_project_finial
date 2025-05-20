package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;

public interface BookTransactionService {
    BookPurchaseResponse createDirectTransaction(BookPurchaseRequest request);
}
