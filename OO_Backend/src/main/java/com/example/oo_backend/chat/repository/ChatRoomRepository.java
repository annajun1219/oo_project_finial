package com.example.oo_backend.chat.service;

import com.example.oo_backend.user.repository.UserRepository;
import com.example.oo_backend.chat.dto.ChatMessageDto;
import com.example.oo_backend.chat.dto.ChatRoomResponseDto;
import com.example.oo_backend.chat.entity.ChatMessage;
import com.example.oo_backend.chat.entity.ChatRoom;
import com.example.oo_backend.chat.repository.ChatMessageRepository;
import com.example.oo_backend.chat.repository.ChatRoomRepository;
import com.example.oo_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // 채팅방 목록 조회
    public List<ChatRoomResponseDto> getChatRooms(User loginUser) {
        List<ChatRoom> rooms = chatRoomRepository.findByUser1OrUser2(loginUser, loginUser);

        return rooms.stream()
                .map(room -> {
                    // 상대방 정보 구하기
                    User otherUser = room.getUser1().equals(loginUser) ? room.getUser2() : room.getUser1();

                    // 마지막 메시지 조회
                    Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByRoomOrderBySentAtDesc(room);

                    return ChatRoomResponseDto.builder()
                            .roomId(room.getId())
                            .otherUserId(otherUser.getUserId().toString())
                            .otherUserName(otherUser.getName())
                            .otherUserProfileImage(otherUser.getProfileImage()) // null일 수도 있음
                            .lastMessage(lastMessageOpt.map(ChatMessage::getContent).orElse(""))
                            .lastSentAt(lastMessageOpt.map(ChatMessage::getSentAt).orElse(null))
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 채팅방 1:1
    public List<ChatMessageDto> getChatMessages(String roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        if (!room.getUser1().getUserId().equals(userId) && !room.getUser2().getUserId().equals(userId)) {
            throw new SecurityException("채팅방 접근 권한이 없습니다.");
        }

        List<ChatMessage> messages = chatMessageRepository.findByRoomOrderBySentAtAsc(room);
        return messages.stream()
                .map(msg -> new ChatMessageDto(msg.getSender().getUserId(), msg.getContent(), msg.getSentAt()))
                .collect(Collectors.toList());
    }

    // 채팅방 1:1 메세지 전송
    public void sendMessage(String roomId, Long senderId, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("보낸 사람을 찾을 수 없습니다."));

        ChatMessage message = new ChatMessage(room, sender, content, LocalDateTime.now());
        chatMessageRepository.save(message);
    }
}

