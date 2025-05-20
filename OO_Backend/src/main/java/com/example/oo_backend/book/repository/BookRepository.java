package com.example.oo_backend.book.repository;

import com.example.oo_backend.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
