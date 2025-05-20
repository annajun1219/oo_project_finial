package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;

public interface BookService {
    BookRegisterResponse registerBook(BookRegisterRequest request);
}
