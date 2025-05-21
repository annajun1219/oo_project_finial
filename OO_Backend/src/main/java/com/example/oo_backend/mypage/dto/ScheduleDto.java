package com.example.oo_backend.mypage.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private String day;       // 월, 화, 수, ...
    private String startTime;
    private String endTime;      // 1교시, 2교시 등
    private String subject;   // 예: 객체지향프로그래밍
    private String professor; // 교수님 이름
}