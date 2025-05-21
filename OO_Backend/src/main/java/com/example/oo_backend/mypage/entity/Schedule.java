package com.example.oo_backend.mypage.entity;

import com.example.oo_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;

    private String startTime;
    private String endTime;

    private String subject;
    private String professor;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}