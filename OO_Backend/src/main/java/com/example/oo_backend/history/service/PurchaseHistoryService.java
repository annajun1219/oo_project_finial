package com.example.oo_backend.history.service;

import com.example.oo_backend.history.dto.PurchaseHistoryResponse;

import java.util.List;

public interface PurchaseHistoryService {
    List<PurchaseHistoryResponse> getPurchaseHistory(Long userId, String status);
}
