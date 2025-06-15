package com.example.oo_backend.book.service;


import com.example.oo_backend.mypage.entity.Schedule;
import com.example.oo_backend.mypage.repository.ScheduleRepository;
import com.example.oo_backend.book.dto.BookDetailResponse;
import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import com.example.oo_backend.book.dto.BookPreviewDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public BookRegisterResponse registerBook(BookRegisterRequest request, MultipartFile image) {
        User user = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Book book = Book.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .professorName(request.getProfessorName())
                .officialPrice(request.getOfficialPrice())
                .price(request.getPrice())
                .description(request.getDescription())
                .sellerId(request.getSellerId())
                .status("판매중")                 // 초기 상태 설정
                .build();

        // 이미지 저장
        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = "book-images/";
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File dest = new File(uploadDir + fileName);
                dest.getParentFile().mkdirs(); // 디렉토리 생성
                image.transferTo(dest);

                book.setImageUrl("/images/" + fileName); // URL 경로로 저장
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }
        }

        bookRepository.save(book);

        BookRegisterResponse response = new BookRegisterResponse();
        response.setBookId(book.getId());
        response.setMessage("교재가 등록되었습니다.");
        return response;

    }

    @Override
    public BookDetailResponse getBookDetail(Long productId, Long viewerId) {
        Book book = bookRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 교재가 존재하지 않습니다."));

        User seller = userRepository.findById(book.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("판매자 정보를 찾을 수 없습니다."));

        BookDetailResponse.SellerInfo sellerInfo = BookDetailResponse.SellerInfo.builder()
                .sellerId(seller.getUserId())
                .name(seller.getName())
                .phone(seller.getPhone())
                .profileImage(seller.getProfileImage())
                .build();

        BookDetailResponse response = BookDetailResponse.builder()
                .productId(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .officialPrice(book.getOfficialPrice())
                .discountRate(
                        (book.getOfficialPrice() != 0)
                                ? (int) Math.round((1 - (double) book.getPrice() / book.getOfficialPrice()) * 100)
                                : 0
                )
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .status(book.getStatus())
                .createdAt(book.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .seller(sellerInfo)
                .isMyPost(viewerId != null && viewerId.equals(book.getSellerId()))
                .build();

        return response;
    }

    @Override
    public List<BookPreviewDto> getBooksByDepartment(String departmentName) {
        return bookRepository.findByCategory(departmentName).stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> getRecommendedBooksBySchedule(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        Set<Book> matchedBooks = new HashSet<>();

        for (Schedule s : schedules) {
            matchedBooks.addAll(bookRepository.findByTitleAndProfessorName(s.getSubject(), s.getProfessor()));
        }

        return matchedBooks.stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> getBooksBySubjectFromSchedule(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        Set<Book> matchedBooks = new HashSet<>();

        for (Schedule s : schedules) {
            matchedBooks.addAll(bookRepository.findByTitleContaining(s.getSubject()));
        }

        return matchedBooks.stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> getBooksByProfessorFromSchedule(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        Set<Book> matchedBooks = new HashSet<>();

        for (Schedule s : schedules) {
            matchedBooks.addAll(bookRepository.findByProfessorNameContaining(s.getProfessor()));
        }

        return matchedBooks.stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> searchByProfessorAndCategory(String keyword, String category) {
        return bookRepository.findByProfessorNameContainingAndCategory(keyword, category)
                .stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> searchByTitleAndCategory(String keyword, String category) {
        return bookRepository.findByTitleContainingAndCategory(keyword, category)
                .stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }
}
