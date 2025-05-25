package com.example.oo_backend.search.service;

import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.search.dto.SearchResultDto;
import com.example.oo_backend.search.entity.SearchHistory;
import com.example.oo_backend.search.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final BookRepository bookRepository;
    private final SearchHistoryRepository historyRepository;

    @Override
    public List<SearchResultDto> searchByTitle(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword)
                .stream().map(book -> new SearchResultDto(
                        book.getId(), book.getTitle(), book.getProfessorName(),
                        book.getCategory(), book.getPrice(), book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchResultDto> searchByProfessor(String keyword) {
        return bookRepository.findByProfessorNameContainingIgnoreCase(keyword)
                .stream().map(book -> new SearchResultDto(
                        book.getId(), book.getTitle(), book.getProfessorName(),
                        book.getCategory(), book.getPrice(), book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveSearchKeyword(Long userId, String keyword) {
        SearchHistory history = new SearchHistory();
        history.setUserId(userId);
        history.setKeyword(keyword);
        historyRepository.save(history);
    }

    @Override
    public List<String> getRecentKeywords(Long userId) {
        return historyRepository.findTop5ByUserIdOrderBySearchedAtDesc(userId)
                .stream().map(SearchHistory::getKeyword)
                .collect(Collectors.toList());
    }
}