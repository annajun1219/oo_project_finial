package com.example.oo_backend.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponseDto {
    private Long roomId;
    private String otherUserId;
    private String otherUserName;
    private String otherUserProfileImage;
    private String lastMessage;
    private LocalDateTime lastSentAt;
}
