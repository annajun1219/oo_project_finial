package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepository transactionRepository;
    private final BookRepository bookRepository;

    @Override
    public BookPurchaseResponse createDirectTransaction(BookPurchaseRequest request) {
        // 교재 정보 조회
        Book book = bookRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 교재를 찾을 수 없습니다."));

        // 거래 저장
        BookTransaction transaction = BookTransaction.builder()
                .buyerId(request.getBuyerId())
                .productId(request.getProductId())
                .price(request.getPrice())
                .paymentMethod(request.getPaymentMethod())
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .status("예약 중")
                .build();
        transactionRepository.save(transaction);

        // 임시 응답
        // 응답 구성
        BookPurchaseResponse response = new BookPurchaseResponse();
        response.setMessage("직거래 요청이 완료되었습니다.");
        response.setTransactionId(transaction.getId());
        response.setProductTitle(book.getTitle());
        response.setStatus(transaction.getStatus());
        response.setSellerId(book.getSellerId());
        response.setBuyerId(request.getBuyerId());

        // contactInfo
        BookPurchaseResponse.ContactInfo contact = new BookPurchaseResponse.ContactInfo();
        contact.setRecipientName(request.getRecipientName());
        contact.setRecipientPhone(request.getRecipientPhone());
        response.setContactInfo(contact);

        return response;
    }

}
