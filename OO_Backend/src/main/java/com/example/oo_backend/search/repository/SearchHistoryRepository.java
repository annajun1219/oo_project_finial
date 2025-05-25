package com.example.oo_backend.search.repository;

import com.example.oo_backend.search.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop5ByUserIdOrderBySearchedAtDesc(Long userId);
}