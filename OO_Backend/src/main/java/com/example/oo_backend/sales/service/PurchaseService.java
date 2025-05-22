package com.example.oo_backend.sales.service;

import com.example.oo_backend.sales.dto.PurchaseHistoryResponse;
import java.util.List;

public interface PurchaseService {
    List<PurchaseHistoryResponse> getPurchaseHistory(Long buyerId, String status);
}
