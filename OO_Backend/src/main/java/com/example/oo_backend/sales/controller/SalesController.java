package com.example.oo_backend.sales.controller;

import com.example.oo_backend.sales.dto.SalesHistoryResponse;
import com.example.oo_backend.sales.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    @GetMapping
    public List<SalesHistoryResponse> getSalesHistory(
            @RequestHeader("userId") Long sellerId,
            @RequestParam(value = "status", required = false) String status
    ) {
        return salesService.getSalesHistory(sellerId, status);
    }
}
