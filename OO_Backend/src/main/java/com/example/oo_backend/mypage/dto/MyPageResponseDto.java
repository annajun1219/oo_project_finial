package com.example.oo_backend.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MyPageResponseDto {

    private Long userId;
    private String name;
    private String profileImage;
    private double rating;
    private int saleCount;
    private int purchaseCount;
    private int warningCount;

    private List<ScheduleDto> scheduleInfo;
}