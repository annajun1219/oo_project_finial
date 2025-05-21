package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;
import com.example.oo_backend.book.dto.BookDetailResponse;

public interface BookService {
    BookRegisterResponse registerBook(BookRegisterRequest request);

    // 교재 상세 조회
    BookDetailResponse getBookDetail(Long productId, Long viewerId);  // viewerId는 null 허용
}
