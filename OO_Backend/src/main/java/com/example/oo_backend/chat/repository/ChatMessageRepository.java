package com.example.oo_backend.chat.repository;

import com.example.oo_backend.chat.entity.ChatMessage;
import com.example.oo_backend.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 채팅방의 가장 마지막 메시지 하나만 가져오기
    Optional<ChatMessage> findTopByRoomOrderBySentAtDesc(ChatRoom room);
    //1:1
    List<ChatMessage> findByRoomOrderBySentAtAsc(ChatRoom room);
}