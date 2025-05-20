package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookRegisterResponse registerBook(BookRegisterRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .professorName(request.getProfessorName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        bookRepository.save(book);

        BookRegisterResponse response = new BookRegisterResponse();
        response.setBookId(book.getId());
        response.setMessage("교재가 등록되었습니다.");
        return response;
    }
}
