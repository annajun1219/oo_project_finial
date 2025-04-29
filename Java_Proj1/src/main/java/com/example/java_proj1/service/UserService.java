package com.example.java_proj1.service;

import com.example.java_proj1.dto.SignupRequestDto;

public abstract class UserService {
    abstract void signup(SignupRequestDto requestDto);
}
