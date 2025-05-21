package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookDetailResponse;
import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;

public interface BookService {
    BookRegisterResponse registerBook(BookRegisterRequest request);

    // 🔽 이 줄을 추가하세요
    BookDetailResponse getBookDetail(Long productId, Long viewerId);
}
