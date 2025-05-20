package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepository transactionRepository;

    @Override
    public BookPurchaseResponse createDirectTransaction(BookPurchaseRequest request) {
        // ① 트랜잭션 저장
        BookTransaction transaction = BookTransaction.builder()
                .buyerId(request.getBuyerId())
                .productId(request.getProductId())
                .price(request.getPrice())
                .paymentMethod(request.getPaymentMethod())
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .status("직거래 요청")
                .build();

        transactionRepository.save(transaction);

        // ② 응답 DTO 생성 - 여기에 실제 값들 설정해줘야 함
        BookPurchaseResponse response = new BookPurchaseResponse();
        response.setMessage("직거래 요청이 완료되었습니다.");
        response.setOriginalPrice(18000);  // 예시 정가
        response.setAveragePrice(16000);   // 예시 평균 시세
        response.setDiscountRate(1 - (double) request.getPrice() / 18000);

        return response;
    }
}
