package com.example.oo_backend.favorite.controller;

import com.example.oo_backend.favorite.dto.BookPreviewDto;
import com.example.oo_backend.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{bookId}")
    public ResponseEntity<String> addFavorite(
            @RequestHeader("userId") Long userId,
            @PathVariable Long bookId
    ) {
        favoriteService.addFavorite(userId, bookId);
        return ResponseEntity.ok("찜이 등록되었습니다.");
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> removeFavorite(
            @RequestHeader("userId") Long userId,
            @PathVariable Long bookId
    ) {
        favoriteService.removeFavorite(userId, bookId);
        return ResponseEntity.ok("찜이 취소되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<BookPreviewDto>> getFavorites(
            @RequestHeader("userId") Long userId
    ) {
        List<BookPreviewDto> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }
}
