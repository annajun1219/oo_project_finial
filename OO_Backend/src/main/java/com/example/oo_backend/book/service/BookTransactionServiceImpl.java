package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepository transactionRepository;

    @Override
    public BookPurchaseResponse createDirectTransaction(UUID buyerId, BookPurchaseRequest request) {
        BookTransaction transaction = BookTransaction.builder()
                .buyerId(buyerId)
                .productId(request.getProductId())
                .price(request.getPrice())
                .paymentMethod(request.getPaymentMethod())
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .status("직거래 요청")
                .build();

        transactionRepository.save(transaction);

        BookPurchaseResponse response = new BookPurchaseResponse();
        response.setMessage("직거래 요청이 완료되었습니다.");
        response.setOriginalPrice(18000);  // 예시
        response.setAveragePrice(16000);   // 예시
        response.setDiscountRate(1 - (double) request.getPrice() / 18000);

        return response;
    }
}
