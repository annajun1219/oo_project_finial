package com.example.oo_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // POST 요청 위해 임시로 꺼둠 (추후 토큰 방식 사용할 경우 필요 없음)
                .formLogin(form -> form.disable()) //  기본 로그인 폼 비활성화
                .httpBasic(basic -> basic.disable()) //  기본 인증 헤더 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**", "/api/chatrooms", "/api/chatrooms/**",
                                "/api/purchase/**", "/api/books/**", "/api/schedule", "/api/mypage", "/api/warning",
                                "/api/recommendation/**", "/api/main", "/api/search/**", "/api/reviews").permitAll()
                        .anyRequest().authenticated()  // 그 외는 인증 필요
                );

        return http.build();
    }
}