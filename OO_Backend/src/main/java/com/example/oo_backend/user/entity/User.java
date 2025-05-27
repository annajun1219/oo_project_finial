package com.example.oo_backend.user.entity;

import com.example.oo_backend.user.entity.UserStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    private LocalDate birth;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;


}