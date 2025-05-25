package com.example.oo_backend.chat.dto;

import lombok.Data;

@Data
public class StartChatRequestDto {
    private Long buyerId;
    private Long sellerId;
    private Long bookId;
}
