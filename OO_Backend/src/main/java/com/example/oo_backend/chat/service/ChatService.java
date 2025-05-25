package com.example.oo_backend.chat.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;
import com.example.oo_backend.book.service.BookTransactionService;
import com.example.oo_backend.chat.dto.ChatMessageDto;
import com.example.oo_backend.chat.dto.ChatRoomResponseDto;
import com.example.oo_backend.chat.dto.StartChatRequestDto;
import com.example.oo_backend.chat.entity.ChatMessage;
import com.example.oo_backend.chat.entity.ChatRoom;
import com.example.oo_backend.chat.repository.ChatMessageRepository;
import com.example.oo_backend.chat.repository.ChatRoomRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
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
    private final BookTransactionService bookTransactionService;

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
                            .otherUserProfileImage(otherUser.getProfileImage())
                            .lastMessage(lastMessageOpt.map(ChatMessage::getContent).orElse(""))
                            .lastSentAt(lastMessageOpt.map(ChatMessage::getSentAt).orElse(null))
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 채팅방 1:1
    public List<ChatMessageDto> getChatMessages(Long roomId, Long userId) {
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
    public void sendMessage(Long roomId, Long senderId, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("보낸 사람을 찾을 수 없습니다."));

        ChatMessage message = new ChatMessage(room, sender, content, LocalDateTime.now());
        chatMessageRepository.save(message);
    }

    // 채팅방 생성 또는 기존 방 불러오기
    public ChatRoomResponseDto startChat(StartChatRequestDto requestDto) {
        Long buyerId = requestDto.getBuyerId();
        Long sellerId = requestDto.getSellerId();
        Long bookId = requestDto.getBookId();

        Optional<ChatRoom> existingRoom = chatRoomRepository
                .findByUser1UserIdAndUser2UserIdAndBookId(buyerId, sellerId, bookId);

        ChatRoom chatRoom = existingRoom.orElseGet(() -> {
            User buyer = userRepository.findById(buyerId)
                    .orElseThrow(() -> new IllegalArgumentException("구매자 정보를 찾을 수 없습니다."));
            User seller = userRepository.findById(sellerId)
                    .orElseThrow(() -> new IllegalArgumentException("판매자 정보를 찾을 수 없습니다."));

            ChatRoom newRoom = ChatRoom.builder()
                    .user1(buyer)
                    .user2(seller)
                    .bookId(bookId)
                    .build();

            return chatRoomRepository.save(newRoom);
        });

        User otherUser = chatRoom.getUser1().getUserId().equals(buyerId) ? chatRoom.getUser2() : chatRoom.getUser1();

        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .otherUserId(otherUser.getUserId().toString())
                .otherUserName(otherUser.getName())
                .otherUserProfileImage(otherUser.getProfileImage())
                .build();
    }

    // 예약 완료 → 구매내역 연동
    public BookPurchaseResponse reserveDirectPurchase(BookPurchaseRequest request) {
        return bookTransactionService.createDirectTransaction(request);
    }
}
