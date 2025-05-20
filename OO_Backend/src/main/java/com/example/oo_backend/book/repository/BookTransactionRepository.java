package com.example.oo_backend.book.repository;

import com.example.oo_backend.book.entity.BookTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long> {
}
