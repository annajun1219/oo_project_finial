package com.example.oo_backend.history.controller;

import com.example.oo_backend.history.dto.PurchaseHistoryResponse;
import com.example.oo_backend.history.service.PurchaseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage/purchases")
public class PurchaseHistoryController {

    private final PurchaseHistoryService purchaseHistoryService;

    @GetMapping
    public List<PurchaseHistoryResponse> getPurchaseHistory(
            @RequestHeader("userId") Long userId,
            @RequestParam(value = "status", required = false) String status) {

        return purchaseHistoryService.getPurchaseHistory(userId, status);
    }
}
