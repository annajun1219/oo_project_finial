package com.example.oo_backend.search.controller;

import com.example.oo_backend.search.service.SearchService;
import com.example.oo_backend.search.dto.SearchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/title")
    public ResponseEntity<List<SearchResultDto>> searchByTitle(
            @RequestParam String keyword, @RequestParam Long userId) {
        searchService.saveSearchKeyword(userId, keyword);
        return ResponseEntity.ok(searchService.searchByTitle(keyword));
    }

    @GetMapping("/professor")
    public ResponseEntity<List<SearchResultDto>> searchByProfessor(
            @RequestParam String keyword, @RequestParam Long userId) {
        searchService.saveSearchKeyword(userId, keyword);
        return ResponseEntity.ok(searchService.searchByProfessor(keyword));
    }

    @GetMapping("/history")
    public ResponseEntity<List<String>> getRecentKeywords(@RequestParam Long userId) {
        return ResponseEntity.ok(searchService.getRecentKeywords(userId));
    }
}