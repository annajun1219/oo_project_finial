package com.example.oo_backend.service;

import com.example.oo_backend.dto.BookPurchaseRequest;
import com.example.oo_backend.dto.BookPurchaseResponse;
import com.example.oo_backend.entity.BookTransaction;
import com.example.oo_backend.repository.BookTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepository transactionRepository;

    @Override
    public BookPurchaseResponse createDirectTransaction(BookPurchaseRequest request) {
        // TODO: 유효성 검사 및 로직 구현 예정

        // 엔티티 생성 및 저장 예시
        BookTransaction transaction = new BookTransaction();
        // set 값들 예시 (setter가 있다고 가정)
        transaction.setBuyerId(request.getBuyerId());
        transaction.setProductId(request.getProductId());
        transaction.setPrice(request.getPrice());
        transaction.setRecipientName(request.getRecipientName());
        transaction.setRecipientPhone(request.getRecipientPhone());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setStatus("직거래 요청");

        transactionRepository.save(transaction);

        // 응답 객체 생성 (필요 시 값 추가)
        return new BookPurchaseResponse();
    }
}
