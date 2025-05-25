package com.example.oo_backend.book.repository;

import com.example.oo_backend.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    int countBySellerId(Long sellerId);

    List<Book> findBySellerIdAndStatus(Long sellerId, String status);

    List<Book> findByCategory(String category);

    Optional<Book> findTopByOrderByCreatedAtDesc();

    // 교재 추천
    List<Book> findByTitleAndProfessorName(String title, String professorName);

    // 추가: 과목명 기반 (부분 일치)
    List<Book> findByTitleContaining(String title);

    // 추가: 교수명 기반 (부분 일치)
    List<Book> findByProfessorNameContaining(String professorName);

    // 추가: 전체 검색
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByProfessorNameContainingIgnoreCase(String professorName);

    // 추가: 단과대별 검색 필터
    List<Book> findByProfessorNameContainingAndCategory(String professorName, String category);
    List<Book> findByTitleContainingAndCategory(String title, String category);

}