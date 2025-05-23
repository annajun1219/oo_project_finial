package com.example.oo_backend.book.repository;

import com.example.oo_backend.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    int countBySellerId(Long sellerId);

    List<Book> findBySellerIdAndStatus(Long sellerId, String status);

    List<Book> findByCategory(String category);

    // 교재 추천
    List<Book> findByTitleAndProfessorName(String title, String professorName);

    // 추가: 과목명 기반 (부분 일치)
    List<Book> findByTitleContaining(String title);

    // 추가: 교수명 기반 (부분 일치)
    List<Book> findByProfessorNameContaining(String professorName);

    // 🔽 추가: 가장 최근 등록된 교재 1개
    Optional<Book> findTopByOrderByCreatedAtDesc();
}
