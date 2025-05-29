
// 로그인 및 회원가입 응답 모델

package com.example.oo_frontend.Model;

import com.google.gson.annotations.SerializedName;
public class Login {
    public boolean success;
    public String email;

    @SerializedName("nickname")
    public String name;
    public int userId;
    public String message;  // 예: 접근 제한 메시지
    public String reason;   // 예: "banned"

    public Login() {}

    public boolean isSuccess() { return success; }
    public String getEmail() { return email; }

    public String getNickname() { return name; }

    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getReason() { return reason; }
}
