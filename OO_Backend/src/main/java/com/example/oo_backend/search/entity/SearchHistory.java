package com.example.oo_backend.search.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String keyword;

    private LocalDateTime searchedAt;

    @PrePersist
    public void onCreate() {
        this.searchedAt = LocalDateTime.now();
    }
}