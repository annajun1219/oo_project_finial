package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;
import com.example.oo_backend.book.dto.BookDetailResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public BookRegisterResponse registerBook(BookRegisterRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .professorName(request.getProfessorName())
                .price(request.getPrice())
                .description(request.getDescription())
                .sellerId(request.getSellerId())
                .build();

        bookRepository.save(book);

        BookRegisterResponse response = new BookRegisterResponse();
        response.setBookId(book.getId());
        response.setMessage("교재가 등록되었습니다.");
        return response;
    }

    @Override
    public BookDetailResponse getBookDetail(Long productId, Long viewerId) {
        Book book = bookRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 교재를 찾을 수 없습니다."));

        User seller = userRepository.findById(book.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));

        BookDetailResponse.SellerInfo sellerInfo = BookDetailResponse.SellerInfo.builder()
                .sellerId(seller.getUserId())
                .nickname(seller.getNickname())
                .profileImage(seller.getProfileImage())
                .warningCount(seller.getWarningCount())
                .build();

        BookDetailResponse response = BookDetailResponse.builder()
                .productId(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .officialPrice(null)
                .averageUsedPrice(null)
                .discountRate(null)
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .status(book.getStatus())
                .createdAt(book.getCreatedAt().toString())
                .seller(sellerInfo)
                .isMyPost(viewerId != null && viewerId.equals(book.getSellerId()))
                .build();

        return response;
    }

}
