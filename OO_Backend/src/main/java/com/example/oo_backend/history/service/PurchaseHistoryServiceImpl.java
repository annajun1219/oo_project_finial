package com.example.oo_backend.history.service;

import com.example.oo_backend.history.dto.PurchaseHistoryResponse;
import com.example.oo_backend.history.entity.PurchaseHistory;
import com.example.oo_backend.history.repository.PurchaseHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseHistoryServiceImpl.class);

    private final PurchaseHistoryRepository purchaseHistoryRepository;

    @Autowired
    public PurchaseHistoryServiceImpl(PurchaseHistoryRepository purchaseHistoryRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    @Override
    public List<PurchaseHistoryResponse> getPurchaseHistory(Long userId, String status) {
        List<PurchaseHistory> histories = (status != null && !status.isEmpty())
                ? purchaseHistoryRepository.findByUserIdAndStatus(userId, status)
                : purchaseHistoryRepository.findByUserId(userId);

        return histories.stream()
                .map(h -> PurchaseHistoryResponse.builder()
                        .bookId(h.getBookId())
                        .title(h.getTitle())
                        .price(h.getPrice())
                        .imageUrl(h.getImageUrl())
                        .datetime(h.getDatetime().toString())
                        .status(h.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}

