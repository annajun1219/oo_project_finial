package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookDetailResponse;
import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;
import com.example.oo_backend.book.dto.BookPreviewDto;

import java.util.List;

public interface BookService {
    BookRegisterResponse registerBook(BookRegisterRequest request);

    // 🔽 이 줄을 추가하세요
    BookDetailResponse getBookDetail(Long productId, Long viewerId);

    List<BookPreviewDto> getBooksByDepartment(String departmentName);

    // 추천 도서 메서드 추가

    List<BookPreviewDto> getRecommendedBooksBySchedule(Long userId);
    List<BookPreviewDto> getBooksBySubjectFromSchedule(Long userId);
    List<BookPreviewDto> getBooksByProfessorFromSchedule(Long userId);

    // 단과대별 검색어 필터
    List<BookPreviewDto> searchByProfessorAndCategory(String keyword, String category);
    List<BookPreviewDto> searchByTitleAndCategory(String keyword, String category);
}