package com.example.oo_backend.warning.controller;

import com.example.oo_backend.warning.dto.WarningRequestDto;
import com.example.oo_backend.warning.dto.WarningResponseDto;
import com.example.oo_backend.warning.service.WarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warning")
@RequiredArgsConstructor
public class WarningController {

    private final WarningService warningService;

    @PostMapping
    public ResponseEntity<WarningResponseDto> warnUser(@RequestBody WarningRequestDto requestDto) {
        WarningResponseDto response = warningService.warnUser(requestDto);
        return ResponseEntity.ok(response);
    }
}