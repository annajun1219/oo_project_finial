package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import com.example.oo_backend.review.repository.ReviewRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.entity.UserStatus;
import com.example.oo_backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;


    @Override
    public BookPurchaseResponse createDirectTransaction(BookPurchaseRequest request) {

        // 교재 정보 조회
        Book book = bookRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 교재를 찾을 수 없습니다."));

        // 구매자 상태 확인
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("구매자 정보를 찾을 수 없습니다."));

        if (buyer.getStatus() == UserStatus.SUSPENDED) {
            throw new IllegalStateException("구매자의 계정은 사용 중지 상태입니다.");
        }

        // 판매자 상태 확인
        User seller = userRepository.findById(book.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("판매자 정보를 찾을 수 없습니다."));

        if (seller.getStatus() == UserStatus.SUSPENDED) {
            throw new IllegalStateException("판매자의 계정은 사용 중지 상태입니다.");
        }


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

        response.setHasReview(false);

        return response;
    }

    @Override
    public List<BookPurchaseResponse> getPurchaseHistory(Long userId, String status) {
        List<BookTransaction> transactions = (status != null && !status.isEmpty())
                ? transactionRepository.findByBuyerIdAndStatus(userId, status)
                : transactionRepository.findByBuyerId(userId);

        return transactions.stream()
                .map(tx -> {
                    boolean hasReview = reviewRepository.existsByTransaction_Id(tx.getId());

                    BookPurchaseResponse res = new BookPurchaseResponse();
                    res.setTransactionId(tx.getId());
                    res.setProductTitle(tx.getProductTitle());
                    res.setStatus(tx.getStatus());
                    res.setSellerId(tx.getSellerId());
                    res.setBuyerId(tx.getBuyerId());
                    res.setHasReview(hasReview);

                    BookPurchaseResponse.ContactInfo contact = new BookPurchaseResponse.ContactInfo();
                    contact.setRecipientName(tx.getRecipientName());
                    contact.setRecipientPhone(tx.getRecipientPhone());
                    res.setContactInfo(contact);

                    return res;
                })
                .collect(Collectors.toList());
    }

}
