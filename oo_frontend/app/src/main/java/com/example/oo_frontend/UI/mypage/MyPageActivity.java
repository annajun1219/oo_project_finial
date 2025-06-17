package com.example.oo_frontend.UI.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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
import com.example.oo_frontend.Model.ScheduleDto;
import com.example.oo_frontend.Model.ScheduleItem; // ✅ 서버에서 온 scheduleInfo 배열의 각 항목
import com.example.oo_frontend.Model.User;
import com.example.oo_frontend.Network.ApiCallback;
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

import java.util.ArrayList;
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
    private List<ScheduleItem> scheduleItems = new ArrayList<>();

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

        btnReport.setOnClickListener(v -> {
            String messageText = (reportMessage != null && !reportMessage.isEmpty())
                    ? reportMessage : "신고 내역이 없습니다.";
            new AlertDialog.Builder(this)
                    .setTitle("신고 내역")
                    .setMessage(messageText)
                    .setPositiveButton("확인", null)
                    .show();
        });

        btnAddTimetable.setOnClickListener(v -> showAddTimetableDialog());
    }

    private void showAddTimetableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.mp_add_timetable, null);

        EditText editSubject = view.findViewById(R.id.editSubject);
        EditText editProfessor = view.findViewById(R.id.editProfessor);
        Spinner spinnerDay = view.findViewById(R.id.spinnerDay);
        Spinner spinnerTime = view.findViewById(R.id.spinnerTime);

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"월", "화", "수", "목", "금"});
        spinnerDay.setAdapter(dayAdapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"09:00", "10:30", "12:00", "13:30", "15:00", "16:30", "18:00"});
        spinnerTime.setAdapter(timeAdapter);

        builder.setView(view)
                .setTitle("시간표 등록")
                .setPositiveButton("등록", (dialog, which) -> {
                    String subject = editSubject.getText().toString().trim();
                    String professor = editProfessor.getText().toString().trim();
                    String day = spinnerDay.getSelectedItem().toString();
                    String startTime = spinnerTime.getSelectedItem().toString();
                    String endTime = getEndTime(startTime); // 종료시간 계산 함수

                    if (subject.isEmpty() || professor.isEmpty()) {
                        Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 🔹 SharedPreferences에서 userId 가져오기
                    SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    Long userId = (long) prefs.getInt("userId", -1);
                    if (userId == -1) {
                        Toast.makeText(this, "userId가 없습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 🔹 UI에 추가할 ScheduleItem 생성
                    ScheduleItem newItem = new ScheduleItem();
                    newItem.setDay(day);
                    newItem.setStartTime(startTime);
                    newItem.setEndTime(endTime);
                    newItem.setSubject(subject);
                    newItem.setProfessor(professor);
                    scheduleItems.add(newItem);
                    showScheduleGrid(scheduleItems);

                    // 🔹 User 모델에 userId만 세팅
                    User user = new User(userId.intValue());

                    // 🔹 수정된 DTO 생성 (User 객체를 첫 인자로)
                    ScheduleDto dto = new ScheduleDto(
                            user,
                            day,
                            startTime,
                            endTime,
                            subject,
                            professor
                    );

                    // 🔹 Retrofit 업로드
                    RetrofitHelper.uploadScheduleItem(this, dto, new ApiCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            Toast.makeText(MyPageActivity.this, "시간표 등록 완료!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(MyPageActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("취소", null)
                .show();
    }


    // ✅ 서버에 시간표 업로드: userId + List<String> scheduleSummary
    private void uploadSchedule(List<String> scheduleSummary) {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1); // long 타입으로 가져오기

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


    private void showScheduleGrid(List<ScheduleItem> scheduleList) {
        timetableGrid.setVisibility(View.VISIBLE);
        timetableGrid.removeAllViews();
        timetableGrid.setColumnCount(6); // 요일 5개 + 교시 라벨 1개
        timetableGrid.setRowCount(8);    // 7교시 + 요일 라벨 1개

        String[] days = {"", "월", "화", "수", "목", "금"};
        String[] times = {"", "09:00", "10:30", "12:00", "13:30", "15:00", "16:30", "18:00"};

        // 1. 헤더 (요일)
        for (int i = 0; i < 6; i++) {
            TextView dayView = new TextView(this);
            dayView.setText(days[i]);
            dayView.setGravity(Gravity.CENTER);
            dayView.setTextSize(14);
            dayView.setTextColor(Color.BLACK);
            dayView.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(0), GridLayout.spec(i)));
            dayView.setPadding(4, 8, 4, 8);
            timetableGrid.addView(dayView);
        }

        // 2. 본문 셀
        for (int row = 1; row <= 7; row++) {
            for (int col = 0; col < 6; col++) {
                if (col == 0) {
                    // 교시 텍스트
                    TextView timeView = new TextView(this);
                    timeView.setText(times[row]);
                    timeView.setGravity(Gravity.CENTER);
                    timeView.setTextSize(12);
                    timeView.setTextColor(Color.DKGRAY);
                    timeView.setLayoutParams(new GridLayout.LayoutParams(
                            GridLayout.spec(row), GridLayout.spec(col)));
                    timeView.setPadding(4, 4, 4, 4);
                    timetableGrid.addView(timeView);
                } else {
                    // 빈 셀 (timetable_cell.xml)
                    View cellView = LayoutInflater.from(this).inflate(R.layout.timetable_cell, null);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                            GridLayout.spec(row), GridLayout.spec(col));
                    params.width = dpToPx(64);
                    params.height = dpToPx(64);
                    cellView.setLayoutParams(params);
                    timetableGrid.addView(cellView);
                }
            }
        }

        // 3. 데이터 채워넣기
        for (ScheduleItem item : scheduleList) {
            int row = getPeriodFromTime(item.getStartTime()); // 1~7
            int col = getDayIndex(item.getDay()); // 1~5

            if (row < 1 || row > 7 || col < 1 || col > 5) continue;

            int index = row * 6 + col;
            View cellView = timetableGrid.getChildAt(index);
            if (cellView != null) {
                TextView tvSubject = cellView.findViewById(R.id.tvSubject);
                TextView tvProfessor = cellView.findViewById(R.id.tvProfessor);
                tvSubject.setText(item.getSubject());
                tvProfessor.setText(item.getProfessor());

                //  내가 입력한 셀에만 배경색 바꾸기
                cellView.setBackgroundResource(R.drawable.timetable_cell_custom_bg);
            }
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    private int getPeriodFromTime(String startTime) {
        switch (startTime) {
            case "09:00": return 1;
            case "10:30": return 2;
            case "12:00": return 3;
            case "13:30": return 4;
            case "15:00": return 5;
            case "16:30": return 6;
            case "18:00": return 7;
            default: return -1;  // 유효하지 않은 시간은 -1 반환
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

    private String getEndTime(String startTime) {
        switch (startTime) {
            case "09:00": return "10:30";
            case "10:30": return "12:00";
            case "12:00": return "13:30";
            case "13:30": return "15:00";
            case "15:00": return "16:30";
            case "16:30": return "18:00";
            case "18:00": return "19:30";
            default: return "";
        }
    }
}

