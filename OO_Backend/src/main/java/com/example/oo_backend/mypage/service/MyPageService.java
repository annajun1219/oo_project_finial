package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.MyPageResponseDto;

public interface MyPageService {
    MyPageResponseDto getMyPageInfo(Long userId);
}
