package com.example.oo_frontend.UI.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oo_frontend.Model.User;
import com.example.oo_frontend.Network.RetrofitClient;
import com.example.oo_frontend.Network.RetrofitService;
import com.example.oo_frontend.R;

public class Signup1Activity extends AppCompatActivity {

    EditText emailFront, password, passwordConfirm;
    EditText phone1, phone2, phone3;
    Button btnCheckEmail, btnNext;

    private boolean isEmailAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        emailFront = findViewById(R.id.editTextEmailFront);
        password = findViewById(R.id.editTextPassword);
        passwordConfirm = findViewById(R.id.editTextPasswordConfirm);
        phone1 = findViewById(R.id.editTextPhone1);
        phone2 = findViewById(R.id.editTextPhone2);
        phone3 = findViewById(R.id.editTextPhone3);
        btnCheckEmail = findViewById(R.id.buttonCheckId);
        btnNext = findViewById(R.id.buttonNext);

        // 이메일 중복 확인
        btnCheckEmail.setOnClickListener(v -> {
            String email = emailFront.getText().toString().trim() + "@sookmyung.ac.kr";
            if (!email.contains("@")) {
                Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            RetrofitService service = RetrofitClient.getClient().create(RetrofitService.class);
            service.checkEmail(email).enqueue(new retrofit2.Callback<Boolean>() {
                @Override
                public void onResponse(retrofit2.Call<Boolean> call, retrofit2.Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean isDuplicate = response.body();
                        if (!isDuplicate) {  // false면 사용 가능
                            isEmailAvailable = true;
                            Toast.makeText(Signup1Activity.this, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show();
                        } else {  // true면 이미 존재
                            isEmailAvailable = false;
                            Toast.makeText(Signup1Activity.this, "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Signup1Activity.this, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Boolean> call, Throwable t) {
                    Toast.makeText(Signup1Activity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        // 다음 버튼 클릭 → 2단계로 이동
        btnNext.setOnClickListener(v -> {
            if (!isEmailAvailable) {
                Toast.makeText(this, "이메일 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = emailFront.getText().toString().trim() + "@sookmyung.ac.kr";
            String pw = password.getText().toString().trim();
            String pw2 = passwordConfirm.getText().toString().trim();
            String phone = phone1.getText().toString().trim() + "-" +
                    phone2.getText().toString().trim() + "-" +
                    phone3.getText().toString().trim();

            if (pw.isEmpty() || pw2.isEmpty() || phone1.getText().toString().isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Signup1Activity.this, Signup2Activity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", pw);
            intent.putExtra("password2", pw2);
            intent.putExtra("phone", phone);
            startActivity(intent);
        });
    }
}
