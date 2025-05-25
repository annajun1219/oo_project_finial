package com.example.oo_backend.warning.repository;

import com.example.oo_backend.warning.entity.Warning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarningRepository extends JpaRepository<Warning, Long> {
    int countByWarnedUserId(Long warnedUserId);
}