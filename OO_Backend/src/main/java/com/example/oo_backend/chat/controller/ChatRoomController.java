package com.example.oo_backend.chat.controller;

import com.example.oo_backend.chat.dto.ChatMessageDto;
import com.example.oo_backend.chat.dto.ChatMessageRequestDto;
import com.example.oo_backend.chat.dto.ChatRoomResponseDto;
import com.example.oo_backend.chat.service.ChatService;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    // JWT 인증이 안 붙어있다면 일단 테스트용 userId 받아서 처리
    @GetMapping
    public ResponseEntity<?> getChatRooms(@RequestHeader("userId") Long userId) {
        User loginUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        List<ChatRoomResponseDto> rooms = chatService.getChatRooms(loginUser);

        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable String roomId, @RequestHeader("userId") Long userId) {
        List<ChatMessageDto> messages = chatService.getChatMessages(roomId, userId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable String roomId,
            @RequestHeader("userId") Long userId,
            @RequestBody ChatMessageRequestDto requestDto) {

        chatService.sendMessage(roomId, userId, requestDto.getMessage());
        return ResponseEntity.ok().build();
    }

}
