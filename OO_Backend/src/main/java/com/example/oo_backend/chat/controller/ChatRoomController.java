package com.example.oo_backend.chat.controller;

import com.example.oo_backend.chat.dto.ChatMessageDto;
import com.example.oo_backend.chat.dto.ChatMessageRequestDto;
import com.example.oo_backend.chat.dto.ChatRoomResponseDto;
import com.example.oo_backend.chat.dto.StartChatRequestDto;
import com.example.oo_backend.chat.service.ChatService;
import com.example.oo_backend.sales.dto.PurchaseHistoryResponse;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    // 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<?> getChatRooms(@RequestHeader("userId") Long userId) {
        User loginUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        List<ChatRoomResponseDto> rooms = chatService.getChatRooms(loginUser);
        return ResponseEntity.ok().body(rooms);
    }

    // 채팅방 메시지 조회
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable Long roomId, @RequestHeader("userId") Long userId) {
        List<ChatMessageDto> messages = chatService.getChatMessages(roomId, userId);
        return ResponseEntity.ok(messages);
    }

    // 채팅방 메시지 전송
    @PostMapping("/{roomId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long roomId,
            @RequestHeader("userId") Long userId,
            @RequestBody ChatMessageRequestDto requestDto) {

        chatService.sendMessage(roomId, userId, requestDto.getMessage());
        return ResponseEntity.ok().build();
    }

    // 채팅방 생성 (1:1)
    @PostMapping("/start")
    public ResponseEntity<ChatRoomResponseDto> startChat(@RequestBody StartChatRequestDto requestDto) {
        ChatRoomResponseDto response = chatService.startChat(requestDto);
        return ResponseEntity.ok(response);
    }

    // 채팅방에서 예약 완료 → 구매내역 연동
    @PostMapping("/{roomId}/reserve")
    public ResponseEntity<BookPurchaseResponse> reserveTransaction(
            @PathVariable String roomId,
            @RequestBody BookPurchaseRequest request) {
        BookPurchaseResponse response = chatService.reserveDirectPurchase(request);
        return ResponseEntity.ok(response);
    }

}


