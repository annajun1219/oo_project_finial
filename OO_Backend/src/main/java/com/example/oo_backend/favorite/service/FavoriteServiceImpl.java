package com.example.oo_backend.favorite.service;

import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.favorite.dto.BookPreviewDto;
import com.example.oo_backend.favorite.entity.Favorite;
import com.example.oo_backend.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;

    @Override
    public void addFavorite(Long userId, Long bookId) {
        if (!favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            Favorite favorite = Favorite.builder()
                    .userId(userId)
                    .bookId(bookId)
                    .build();
            favoriteRepository.save(favorite);
        }
    }

    @Override
    public void removeFavorite(Long userId, Long bookId) {
        favoriteRepository.deleteByUserIdAndBookId(userId, bookId);
    }

    @Override
    public List<BookPreviewDto> getFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        return favorites.stream()
                .map(fav -> bookRepository.findById(fav.getBookId()).orElse(null))
                .filter(Objects::nonNull)
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getImageUrl(),
                        book.getStatus(),
                        book.getCreatedAt() != null ? book.getCreatedAt().toLocalDate() : null
                ))
                .collect(Collectors.toList());
    }
}
