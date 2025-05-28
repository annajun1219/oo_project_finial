
// 회원가입 요청 모델
package com.example.oo_frontend.Model;

public class Signup {
    private String name; // 닉네임
    private String email;
    private String password;
    private String password2;
    private String birth;
    private String phone;

    public Signup(String name, String email, String password, String password2, String birth, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.birth = birth;
        this.phone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPassword2() { return password2; }
    public void setPassword2(String password2) { this.password2 = password2; }
    public String getBirth() { return birth; }
    public void setBirth(String birth) { this.birth = birth; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
