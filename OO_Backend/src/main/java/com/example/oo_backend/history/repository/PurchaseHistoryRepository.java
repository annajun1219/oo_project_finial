package com.example.oo_backend.history.repository;

import com.example.oo_backend.history.entity.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByUserId(Long userId);
    List<PurchaseHistory> findByUserIdAndStatus(Long userId, String status);
}
