package com.example.oo_backend.warning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarningRequestDto {
    private Long warnerId;
    private Long warnedUserId;
    private String reason;  // ex) 비방 및 욕설, 부적절한 사진, 무통보 거래 파기
}