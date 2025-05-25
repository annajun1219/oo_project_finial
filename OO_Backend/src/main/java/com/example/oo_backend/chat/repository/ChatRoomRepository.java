package com.example.oo_backend.chat.repository;

import com.example.oo_backend.chat.entity.ChatRoom;
import com.example.oo_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    // 현재 로그인한 사용자가 참여 중인 채팅방 전체 조회
    List<ChatRoom> findByUser1OrUser2(User user1, User user2);

    Optional<ChatRoom> findByUser1UserIdAndUser2UserIdAndBookId(Long user1Id, Long user2Id, Long bookId);

}
