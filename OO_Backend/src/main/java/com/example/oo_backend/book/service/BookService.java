package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookDetailResponse;
import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;

import java.util.List;
import com.example.oo_backend.book.dto.BookPreviewDto;

public interface BookService {
    BookRegisterResponse registerBook(BookRegisterRequest request);

    // 🔽 이 줄을 추가하세요
    BookDetailResponse getBookDetail(Long productId, Long viewerId);

    List<BookPreviewDto> getBooksByDepartment(String departmentName);

    List<BookPreviewDto> getRecommendedBooksBySchedule(Long userId);
    List<BookPreviewDto> getBooksBySubjectFromSchedule(Long userId);
    List<BookPreviewDto> getBooksByProfessorFromSchedule(Long userId);
}
