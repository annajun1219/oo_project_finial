package com.example.oo_backend.sales.service;

import com.example.oo_backend.sales.dto.SalesHistoryResponse;

import java.util.List;

public interface SalesService {
    List<SalesHistoryResponse> getSalesHistory(Long sellerId, String status);

    void updateTransactionStatus(Long transactionId, String newStatus);
}
