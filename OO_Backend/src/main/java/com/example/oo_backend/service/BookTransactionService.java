package com.example.oo_backend.service;

import com.example.oo_backend.dto.BookPurchaseRequest;
import com.example.oo_backend.dto.BookPurchaseResponse;

public interface BookTransactionService {
    BookPurchaseResponse createDirectTransaction(BookPurchaseRequest request);
}
