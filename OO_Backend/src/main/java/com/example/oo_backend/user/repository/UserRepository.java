package com.example.oo_backend.user.repository;

import com.example.oo_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 휴대폰 번호 중복 확인
    boolean existsByPhone(String phone);

    // 닉네임(아이디) 중복 확인
    boolean existsByName(String name);

    Optional<User> findByEmail(String email);
}