package com.example.oo_backend.chat.service;

import com.example.oo_backend.chat.dto.ChatRoomResponseDto;
import com.example.oo_backend.chat.dto.StartChatRequestDto;
import com.example.oo_backend.chat.entity.ChatRoom;
import com.example.oo_backend.chat.repository.ChatRoomRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoomResponseDto startChat(StartChatRequestDto requestDto) {
        Optional<ChatRoom> existingRoom = chatRoomRepository
                .findByUser1UserIdAndUser2UserIdAndBookId(
                        requestDto.getBuyerId(), requestDto.getSellerId(), requestDto.getBookId());

        ChatRoom chatRoom = existingRoom.orElseGet(() -> {
            User buyer = userRepository.findById(requestDto.getBuyerId())
                    .orElseThrow(() -> new IllegalArgumentException("구매자 정보를 찾을 수 없습니다."));
            User seller = userRepository.findById(requestDto.getSellerId())
                    .orElseThrow(() -> new IllegalArgumentException("판매자 정보를 찾을 수 없습니다."));

            ChatRoom newRoom = ChatRoom.builder()
                    .user1(buyer)
                    .user2(seller)
                    .bookId(requestDto.getBookId())
                    .build();
            return chatRoomRepository.save(newRoom);
        });

        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .otherUserId(chatRoom.getUser2().getUserId().toString()) // 예시
                .otherUserName(chatRoom.getUser2().getName())
                .otherUserProfileImage(chatRoom.getUser2().getProfileImage())
                .lastMessage("") // 초기 채팅이 없으니까 빈 값
                .lastSentAt(null)
                .build();
    }
}
