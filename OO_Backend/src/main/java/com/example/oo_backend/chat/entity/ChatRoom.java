package com.example.oo_backend.chat.entity;

import com.example.oo_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 채팅방 고유 ID (예: UUID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id")
    private User user1;  // 참여자 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id")
    private User user2;  // 참여자 2

    private Long bookId; // 책 ID 추가 (있다면)

    // 생성일자 등 필요한 추가 필드도 여기에 작성
}
