
// 로그인 및 회원가입 응답 모델

package com.example.oo_frontend.Model;

public class Login {
    public boolean success;
    public String email;
    public String nickname;
    public int userId;
    public String message;  // 예: 접근 제한 메시지
    public String reason;   // 예: "banned"

    public Login() {}

    public boolean isSuccess() { return success; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getReason() { return reason; }
}
