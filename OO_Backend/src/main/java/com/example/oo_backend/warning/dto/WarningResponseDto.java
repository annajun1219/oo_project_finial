package com.example.oo_backend.warning.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WarningResponseDto {
    private String message;  // ex) 홍길동님이 '비방 및 욕설' 사유로 경고하였습니다.
}