package com.example.oo_backend.favorite.service;

import com.example.oo_backend.favorite.dto.BookPreviewDto;
import java.util.List;

public interface FavoriteService {

    void addFavorite(Long userId, Long bookId);

    void removeFavorite(Long userId, Long bookId);

    List<BookPreviewDto> getFavorites(Long userId);
}
