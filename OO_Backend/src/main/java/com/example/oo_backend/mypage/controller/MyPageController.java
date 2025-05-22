package com.example.oo_backend.mypage.controller;

import com.example.oo_backend.mypage.dto.MyPageResponseDto;
import com.example.oo_backend.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<MyPageResponseDto> getMyPageInfo(@RequestHeader("userId") Long userId) {
        MyPageResponseDto response = myPageService.getMyPageInfo(userId);
        return ResponseEntity.ok(response);
    }
}
