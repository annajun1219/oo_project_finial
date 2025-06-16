package com.example.oo_frontend.UI.auth;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oo_frontend.Model.Signup;
import com.example.oo_frontend.Model.Login;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.Network.RetrofitClient;
import com.example.oo_frontend.Network.RetrofitService;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.main.MainActivity;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup2Activity extends AppCompatActivity {

    private ImageView imageProfile;
    private EditText editNickname, editBirth;
    private ImageButton btnSelectDate;
    private Button btnDone, btnCheckId;
    private Uri selectedImageUri;

    // 1단계에서 전달받은 데이터
    private String email, password, password2, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        // 1단계에서 넘어온 데이터 받기
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        password2 = intent.getStringExtra("password2");
        phone = intent.getStringExtra("phone");

        // 뷰 연결
        imageProfile = findViewById(R.id.imageProfile);
        editNickname = findViewById(R.id.editNickname);
        editBirth = findViewById(R.id.tv_selected_date);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnDone = findViewById(R.id.buttonNext);
        btnCheckId = findViewById(R.id.buttonCheckId); // 닉네임 중복 확인 버튼

        // 날짜 선택 다이얼로그
        btnSelectDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String birth = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                editBirth.setText(birth);
                editBirth.setTextColor(Color.BLACK);
            }, y, m, d).show();
        });

        // 갤러리에서 프로필 이미지 선택 (현재 서버 전송은 구현 X)
        findViewById(R.id.btnAddPhoto).setOnClickListener(v -> {
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/*");
            startActivityForResult(pickIntent, 100);
        });

        // 닉네임 중복 확인 (임시 로직)
        btnCheckId.setOnClickListener(v -> {
            String nickname = editNickname.getText().toString().trim();
            if (nickname.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                checkNicknameDuplicate(nickname);
            }
        });

        // 회원가입 완료 버튼 클릭
        btnDone.setOnClickListener(v -> {
            String nickname = editNickname.getText().toString().trim();
            String birth = editBirth.getText().toString().trim();

            // 필수 항목 검증
            if (nickname.isEmpty() || birth.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Signup 모델 객체 생성
            Signup signupData = new Signup(nickname, email, password, password2, birth, phone);

            // ✅ Retrofit을 통해 서버에 회원가입 요청
            RetrofitService service = RetrofitClient.getClient().create(RetrofitService.class);
            service.signup(signupData).enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Login result = response.body();

                        Toast.makeText(Signup2Activity.this, result.getNickname() + "님 환영합니다!", Toast.LENGTH_SHORT).show();

                        // 로그인 완료 후 메인 페이지 이동
                        Intent intent = new Intent(Signup2Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Signup2Activity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Toast.makeText(Signup2Activity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Signup", "회원가입 실패", t);
                }
            });
        });
    }

    // 이미지 선택 결과 처리 (프로필 사진)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageProfile.setImageURI(selectedImageUri); // 선택한 이미지 표시
        }
    }

    // 닉네임 중복 확인 - 실제 서버 요청 필요
    private void checkNicknameDuplicate(String nickname) {
        RetrofitService api = RetrofitHelper.getApiService();  // RetrofitClient 의 getApiService() 사용
        api.checkName(nickname).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isTaken = response.body();
                    if (!isTaken) {
                        Toast.makeText(Signup2Activity.this,
                                "사용 가능한 닉네임입니다.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Signup2Activity.this,
                                "이미 사용 중인 닉네임입니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Signup2Activity.this,
                            "닉네임 확인 실패: 코드 " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(Signup2Activity.this,
                        "네트워크 오류: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
