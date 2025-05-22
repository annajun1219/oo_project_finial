package com.example.oo_backend.book.repository;

import com.example.oo_backend.book.entity.BookTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long> {
    int countByBuyerId(Long buyerId);

    List<BookTransaction> findBySellerIdAndStatus(Long sellerId, String status);
    List<BookTransaction> findByBuyerId(Long buyerId);

    List<BookTransaction> findByBuyerIdAndStatus(Long buyerId, String status);

}
