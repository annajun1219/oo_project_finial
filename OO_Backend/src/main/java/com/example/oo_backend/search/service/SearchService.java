package com.example.oo_backend.search.service;

import com.example.oo_backend.search.dto.SearchResultDto;
import java.util.List;

public interface SearchService {
    List<SearchResultDto> searchByTitle(String keyword);
    List<SearchResultDto> searchByProfessor(String keyword);
    void saveSearchKeyword(Long userId, String keyword);
    List<String> getRecentKeywords(Long userId);
}