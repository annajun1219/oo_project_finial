package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


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
                .productTitle(book.getTitle())
                .sellerId(book.getSellerId())
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

    @Override
    public List<BookPurchaseResponse> getPurchaseHistory(Long userId, String status) {
        List<BookTransaction> transactions = (status != null && !status.isEmpty())
                ? transactionRepository.findByBuyerIdAndStatus(userId, status)
                : transactionRepository.findByBuyerId(userId);

        return transactions.stream()
                .map(tx -> {
                    BookPurchaseResponse res = new BookPurchaseResponse();
                    res.setTransactionId(tx.getId());
                    res.setProductTitle(tx.getProductTitle());
                    res.setStatus(tx.getStatus());
                    res.setSellerId(tx.getSellerId());
                    res.setBuyerId(tx.getBuyerId());

                    BookPurchaseResponse.ContactInfo contact = new BookPurchaseResponse.ContactInfo();
                    contact.setRecipientName(tx.getRecipientName());
                    contact.setRecipientPhone(tx.getRecipientPhone());
                    res.setContactInfo(contact);

                    return res;
                })
                .collect(Collectors.toList());
    }

}
