package com.example.oo_backend.mypage.repository;

import com.example.oo_backend.mypage.entity.Schedule;
import com.example.oo_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUser(User user);
}