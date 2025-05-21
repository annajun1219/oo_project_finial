package com.example.oo_backend.chat.entity;

import com.example.oo_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    @Id
    private String id;  // 채팅방 고유 ID (예: UUID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id")
    private User user1;  // 참여자 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id")
    private User user2;  // 참여자 2

    // 필요하다면 생성일자 등 필드 추가 가능
}



