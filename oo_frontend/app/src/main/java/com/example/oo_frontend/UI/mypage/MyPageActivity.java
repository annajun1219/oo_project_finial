package com.example.oo_frontend.UI.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.MyPage;
import com.example.oo_frontend.Model.ScheduleItem; // ✅ 서버에서 온 scheduleInfo 배열의 각 항목
import com.example.oo_frontend.Network.RetrofitClient;
import com.example.oo_frontend.Network.RetrofitService;
import com.example.oo_frontend.R;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.UI.mypage.buy.BuyHistoryActivity;
import com.example.oo_frontend.UI.mypage.favorite.FavoritesActivity;
import com.example.oo_frontend.UI.mypage.review.ReviewActivity;
import com.example.oo_frontend.UI.mypage.sales.SalesHistoryActivity;
import com.example.oo_frontend.UI.main.MainActivity;
import com.example.oo_frontend.UI.chat.list.ChatListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyPageActivity extends AppCompatActivity {

    private ImageView imageProfile;
    private TextView tvUserName, tvScore, tvReview, tvSellCount, tvBuyCount, tvReportCount;
    private ImageView btnHeart, btnReport;
    private LinearLayout btnSell, btnBuy, btnAddTimetable;
    private GridLayout timetableGrid;

    private String reportReason = "";
    private String reportMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // ✅ 뷰 초기화
        imageProfile = findViewById(R.id.imageProfile);
        tvUserName = findViewById(R.id.tvUserName);
        tvScore = findViewById(R.id.tvScore);
        tvReview = findViewById(R.id.tvReview);
        tvSellCount = findViewById(R.id.cell_num);
        tvBuyCount = findViewById(R.id.purchase_num);
        tvReportCount = findViewById(R.id.tvReportCount);
        btnHeart = findViewById(R.id.icHeart);
        btnReport = findViewById(R.id.icReport);
        btnSell = findViewById(R.id.cardSell);
        btnBuy = findViewById(R.id.cardBuy);
        btnAddTimetable = findViewById(R.id.btnAddTimetable);
        timetableGrid = findViewById(R.id.timetableGrid);

        loadMyPageData();   // ✅ 서버에서 유저 정보 로딩
        setClickEvents();   // ✅ 클릭 이벤트 설정

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(MyPageActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(MyPageActivity.this, ChatListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });

    }

    private void loadMyPageData() {
        // ✅ 저장된 userId 불러오기
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        // ✅ userId가 없으면 에러 처리
        if (userId == -1) {
            Toast.makeText(this, "userId가 없습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ API 요청
        RetrofitService api = RetrofitHelper.getApiService();
        Call<MyPage> call = api.getMyPage(userId); // ✅ userId 넘기기

        call.enqueue(new Callback<MyPage>() {
            @Override
            public void onResponse(Call<MyPage> call, Response<MyPage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyPage data = response.body();

                    // 프로필 이미지
                    Glide.with(MyPageActivity.this)
                            .load(data.getProfileImage())
                            .into(imageProfile);

                    tvUserName.setText(data.getName());
                    tvScore.setText(String.valueOf(data.getRating()));
                    tvSellCount.setText(String.valueOf(data.getSaleCount()));
                    tvBuyCount.setText(String.valueOf(data.getPurchaseCount()));
                    tvReportCount.setText(String.valueOf(data.getWarningCount()));

                    reportReason = data.getReportReason();
                    reportMessage = data.getMessage();

                    // ✅ 시간표 표시
                    showScheduleGrid(data.getScheduleInfo());

                    SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    boolean alreadyShown = prefs.getBoolean("report_shown", false);

                    // 신고 메시지 첫 로드 시 알림 표시
                    if (!alreadyShown && reportMessage != null && !reportMessage.isEmpty()) {
                        new AlertDialog.Builder(MyPageActivity.this)
                                .setTitle("신고 알림")
                                .setMessage(reportMessage)
                                .setPositiveButton("확인", null)
                                .show();
                        prefs.edit().putBoolean("report_shown", true).apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyPage> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "서버 통신 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickEvents() {
        tvReview.setOnClickListener(v -> startActivity(new Intent(this, ReviewActivity.class)));
        btnHeart.setOnClickListener(v -> startActivity(new Intent(this, FavoritesActivity.class)));
        btnSell.setOnClickListener(v -> startActivity(new Intent(this, SalesHistoryActivity.class)));
        btnBuy.setOnClickListener(v -> startActivity(new Intent(this, BuyHistoryActivity.class)));

        // 신고 내역 팝업
        btnReport.setOnClickListener(v -> {
            String messageText = (reportMessage != null && !reportMessage.isEmpty())
                    ? reportMessage : "신고 내역이 없습니다.";
            new AlertDialog.Builder(this)
                    .setTitle("신고 내역")
                    .setMessage(messageText)
                    .setPositiveButton("확인", null)
                    .show();
        });

        // 시간표 등록 버튼
        btnAddTimetable.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(this);
            View dialogView = inflater.inflate(R.layout.mp_add_timetable, null);

            Spinner daySpinner = dialogView.findViewById(R.id.spinnerDay);
            Spinner timeSpinner = dialogView.findViewById(R.id.spinnerTime);
            EditText editSubject = dialogView.findViewById(R.id.editSubject);
            EditText editProfessor = dialogView.findViewById(R.id.editProfessor);

            ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"월", "화", "수", "목", "금"});
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            daySpinner.setAdapter(dayAdapter);

            ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"1교시", "2교시", "3교시", "4교시"});
            timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeSpinner.setAdapter(timeAdapter);

            new AlertDialog.Builder(this)
                    .setTitle("시간표 등록")
                    .setView(dialogView)
                    .setPositiveButton("등록", (dialog, which) -> {
                        String subject = editSubject.getText().toString();
                        String professor = editProfessor.getText().toString();
                        String day = daySpinner.getSelectedItem().toString();
                        String time = timeSpinner.getSelectedItem().toString();
                        String scheduleText = day + " " + time + " " + subject + " (" + professor + ")";

                        // ✅ 서버 요구 형식대로 문자열을 리스트로 감싸서 넘김
                        List<String> scheduleList = new java.util.ArrayList<>();
                        scheduleList.add(scheduleText);
                        uploadSchedule(scheduleList);
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });
    }

    // ✅ 서버에 시간표 업로드: userId + List<String> scheduleSummary
    private void uploadSchedule(List<String> scheduleSummary) {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);


        if (userId == -1) {
            Toast.makeText(this, "userId 없음", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitService api = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Void> call = api.uploadSchedule(userId, scheduleSummary);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyPageActivity.this, "등록 완료", Toast.LENGTH_SHORT).show();

                    loadMyPageData();
                } else {
                    Toast.makeText(MyPageActivity.this, "등록 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // 시간표를 그리드에 표시
    private void showScheduleGrid(List<ScheduleItem> scheduleList) {
        timetableGrid.removeAllViews();
        timetableGrid.setColumnCount(6);
        timetableGrid.setRowCount(5);

        String[] days = {"", "월", "화", "수", "목", "금"};

        for (String day : days) {
            TextView tv = new TextView(this);
            tv.setText(day);
            tv.setPadding(8, 8, 8, 8);
            timetableGrid.addView(tv);
        }

        for (int row = 1; row <= 4; row++) {
            for (int col = 0; col < 6; col++) {
                TextView cell = new TextView(this);
                cell.setPadding(8, 8, 8, 8);
                cell.setBackgroundResource(R.drawable.cell_border);
                if (col == 0) cell.setText(row + "교시");
                timetableGrid.addView(cell);
            }
        }

        for (ScheduleItem item : scheduleList) {
            String day = item.getDay();
            String subject = item.getSubject();
            int row = getPeriodFromTime(item.getStartTime());
            int col = getDayIndex(day);
            int index = row * 6 + col;

            if (index < timetableGrid.getChildCount() && col > 0) {
                TextView targetCell = (TextView) timetableGrid.getChildAt(index);
                targetCell.setText(subject);
            }

        }
    }

    // 시간표 시간 → 교시로 매핑
    private int getPeriodFromTime(String startTime) {
        switch (startTime) {
            case "09:00": return 1;
            case "10:45": return 2;
            case "13:00": return 3;
            case "14:45": return 4;
            default: return 1;
        }
    }

    private int getDayIndex(String day) {
        switch (day) {
            case "월":
            case "월요일": return 1;
            case "화":
            case "화요일": return 2;
            case "수":
            case "수요일": return 3;
            case "목":
            case "목요일": return 4;
            case "금":
            case "금요일": return 5;
            default: return -1;
        }
    }

}

