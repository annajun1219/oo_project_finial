package com.example.oo_backend.controller;

import com.example.oo_backend.dto.SignupRequest;
import com.example.oo_backend.entity.User;
import com.example.oo_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        User user = userService.register(request);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        // response.put("token", jwtToken); // JWT 사용 시 여기에 추가

        return ResponseEntity.ok(response);
    }
}

