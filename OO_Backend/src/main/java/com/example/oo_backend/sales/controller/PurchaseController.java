package com.example.oo_backend.sales.controller;

import com.example.oo_backend.sales.dto.PurchaseHistoryResponse;
import com.example.oo_backend.sales.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<PurchaseHistoryResponse>> getPurchaseHistory(
            @RequestHeader("userId") Long buyerId,
            @RequestParam(value = "status", required = false) String status
    ) {
        List<PurchaseHistoryResponse> response = purchaseService.getPurchaseHistory(buyerId, status);
        return ResponseEntity.ok(response);
    }
}
