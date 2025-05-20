package com.example.oo_backend.service;

import com.example.oo_backend.dto.BookRegisterRequest;
import com.example.oo_backend.dto.BookRegisterResponse;

public interface BookService {
    BookRegisterResponse registerBook(BookRegisterRequest request);
}
