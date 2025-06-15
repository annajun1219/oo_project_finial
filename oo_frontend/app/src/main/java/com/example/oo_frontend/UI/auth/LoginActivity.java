package com.example.oo_frontend.UI.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.example.oo_frontend.Model.Login;
import com.example.oo_frontend.Network.RetrofitClient;
import com.example.oo_frontend.Network.RetrofitService;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button loginButton;
    private TextView textLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 뷰 바인딩
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        textLinks = findViewById(R.id.textLinks);

        // 회원가입 화면으로 이동
        textLinks.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Signup1Activity.class);
            startActivity(intent);
        });

        // 로그인 시도
        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 요청 데이터 준비
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        // 서버 요청
        RetrofitService service = RetrofitClient.getClient().create(RetrofitService.class);
        service.login(loginData).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Login result = response.body();

                    // 접근 제한 처리
                    if (!result.isSuccess() && "banned".equals(result.getReason())) {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("접근 제한")
                                .setMessage(result.getMessage() != null ? result.getMessage() : "접근이 제한된 계정입니다.")
                                .setPositiveButton("확인", null)
                                .show();
                        return;
                    }

                    // 로그인 성공 시 사용자 정보 저장
                    SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putInt("userId", result.getUserId())
                            .putString("nickname", result.getNickname()) // 👈 추가!
                            .apply();

                    Toast.makeText(getApplicationContext(), result.getNickname() + "님 로그인 성공!", Toast.LENGTH_SHORT).show();

                    // 메인화면 이동
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "서버 응답 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "로그인 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

