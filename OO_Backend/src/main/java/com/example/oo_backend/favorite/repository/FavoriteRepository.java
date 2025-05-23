package com.example.oo_backend.favorite.repository;

import com.example.oo_backend.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    List<Favorite> findByUserId(Long userId);

    void deleteByUserIdAndBookId(Long userId, Long bookId);
}
