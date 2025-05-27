package com.example.oo_backend.warning.service;

import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.entity.UserStatus;
import com.example.oo_backend.user.repository.UserRepository;
import com.example.oo_backend.warning.dto.WarningRequestDto;
import com.example.oo_backend.warning.dto.WarningResponseDto;
import com.example.oo_backend.warning.entity.Warning;
import com.example.oo_backend.warning.repository.WarningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarningServiceImpl implements WarningService {

    private final WarningRepository warningRepository;
    private final UserRepository userRepository;

    @Override
    public WarningResponseDto warnUser(WarningRequestDto requestDto) {
        User warner = userRepository.findById(requestDto.getWarnerId())
                .orElseThrow(() -> new IllegalArgumentException("경고자를 찾을 수 없습니다."));
        User warned = userRepository.findById(requestDto.getWarnedUserId())
                .orElseThrow(() -> new IllegalArgumentException("경고 대상자를 찾을 수 없습니다."));

        // 경고 저장
        Warning warning = Warning.builder()
                .warnerId(warner.getUserId())
                .warnedUserId(warned.getUserId())
                .reason(requestDto.getReason())
                .build();

        warningRepository.save(warning);

        // 누적 경고 수 계산
        int warningCount = warningRepository.countByWarnedUserId(warned.getUserId());

        // 2회 이상이면 상태 SUSPENDED로 변경
        if (warningCount >= 2 && warned.getStatus() != UserStatus.SUSPENDED) {
            warned.setStatus(UserStatus.SUSPENDED);
            userRepository.save(warned);
        }

        String message = warner.getName() + "님이 '" + requestDto.getReason() + "' 사유로 경고하였습니다.";

        return WarningResponseDto.builder()
                .message(message)
                .build();
    }
}
