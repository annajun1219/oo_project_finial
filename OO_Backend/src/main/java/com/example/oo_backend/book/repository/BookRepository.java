package com.example.oo_backend.book.repository;

import com.example.oo_backend.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    int countBySellerId(Long sellerId);

    List<Book> findBySellerIdAndStatus(Long sellerId, String status);

}
