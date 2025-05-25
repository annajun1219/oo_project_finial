package com.example.oo_backend.warning.service;

import com.example.oo_backend.warning.dto.WarningRequestDto;
import com.example.oo_backend.warning.dto.WarningResponseDto;

public interface WarningService {
    WarningResponseDto warnUser(WarningRequestDto requestDto);
}
