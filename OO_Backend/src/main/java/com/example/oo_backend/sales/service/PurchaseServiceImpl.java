package com.example.oo_backend.sales.service;

import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import com.example.oo_backend.sales.dto.PurchaseHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final BookTransactionRepository bookTransactionRepository;

    @Override
    public List<PurchaseHistoryResponse> getPurchaseHistory(Long buyerId, String status) {
        List<BookTransaction> transactions = (status != null && !status.isEmpty())
                ? bookTransactionRepository.findByBuyerIdAndStatus(buyerId, status)
                : bookTransactionRepository.findByBuyerId(buyerId);

        return transactions.stream()
                .map(tx -> new PurchaseHistoryResponse(
                        tx.getProductId(),
                        tx.getProductTitle(),
                        tx.getPrice(),
                        tx.getStatus(),
                        tx.getCreatedAt().toLocalDate()
                ))
                .collect(Collectors.toList());
    }
}
