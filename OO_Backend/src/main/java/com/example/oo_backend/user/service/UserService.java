package com.example.oo_backend.user.service;

import com.example.oo_backend.user.dto.SignupRequest;
import com.example.oo_backend.user.dto.LoginRequest;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public User register(SignupRequest request) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 일치 확인
        if (!request.getPassword().equals(request.getPassword2())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 사용자 생성 및 저장
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setBirth(request.getBirth());

        return userRepository.save(user);
    }

    // 로그인
    public Map<String, Object> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("nickname", user.getName());
        response.put("email", user.getEmail());

        return response;
    }

    // 사용자 이름 조회
    public String getUserName(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."))
                .getName();
    }

    // 사용자 탈퇴
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }

    // 이메일 중복 확인
    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 휴대폰 번호 중복 확인
    public boolean checkPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    // 닉네임(아이디) 중복 확인
    public boolean checkName(String name) {
        return userRepository.existsByName(name);
    }

}