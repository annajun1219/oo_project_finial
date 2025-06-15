package com.example.oo_frontend.UI.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.chat.list.ChatListActivity;
import com.example.oo_frontend.UI.main.book.BookListAllActivity;
import com.example.oo_frontend.UI.main.book.BookListByCategoryActivity;
import com.example.oo_frontend.UI.main.recommendation.BookRecommendActivity;
import com.example.oo_frontend.UI.main.book.BookRegisterActivity;
import com.example.oo_frontend.UI.main.category.CategoryEtcFragment;
import com.example.oo_frontend.UI.main.category.CategoryMajorFragment;
import com.example.oo_frontend.UI.mypage.MyPageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 categoryViewPager;
    private TextView nameTextView; // ✅ 사용자 이름 표시용 (layout에 있어야 함)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ 사용자 ID 가져오기
        int userId = getSharedPreferences("loginPrefs", MODE_PRIVATE).getInt("userId", -1);

        // ✅ 사용자 이름 표시용 TextView (activity_main.xml에 id가 nameTextView인 요소 필요)
        nameTextView = findViewById(R.id.nameTextView);

        // ✅ 메인페이지 데이터 불러오기
        RetrofitHelper.fetchMainPageRaw(this, userId, new ApiCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject body) {
                // 이름 표시
                String name = body.get("name").getAsString();
                nameTextView.setText(name + "님");

                // 카테고리 로그 출력 (추후 Fragment와 연동 가능)
                JsonArray categoryArray = body.getAsJsonArray("categories");
                for (int i = 0; i < categoryArray.size(); i++) {
                    Log.d("카테고리", categoryArray.get(i).getAsString());
                }

                if (body.has("recommendation") && !body.get("recommendation").isJsonNull()) {
                    JsonObject rec = body.getAsJsonObject("recommendation");
                    String title = rec.get("title").getAsString();
                    int price = rec.get("price").getAsInt();
                    Log.d("추천 교재", title + " / " + price + "원");
                } else {
                    Log.d("추천 교재", "추천 정보 없음");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MainActivity.this, "메인 데이터 로딩 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ ViewPager2 연결
        categoryViewPager = findViewById(R.id.categoryViewPager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CategoryMajorFragment());
        fragments.add(new CategoryEtcFragment());

        FragmentStateAdapter pagerAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        };
        categoryViewPager.setAdapter(pagerAdapter);

        // 등록 버튼
        ImageButton fabRegister = findViewById(R.id.fabRegister);
        fabRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, BookRegisterActivity.class));
        });

        // 검색 아이콘
        ImageView searchIcon = findViewById(R.id.registerIcon);
        searchIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, BookListAllActivity.class));
        });

        // 시스템바 패딩
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 추천 교재 바로가기
        Button recommendBtn = findViewById(R.id.recommendBtn);
        recommendBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookRecommendActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // 바텀 네비게이션
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatListActivity.class));
                return true;
            }
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, MyPageActivity.class));
                return true;
            }
            return false;
        });
    }

    // ✅ 카테고리 클릭 시 교재 목록 페이지 이동
    public void onCategoryClicked(String categoryName) {
        Intent intent = new Intent(this, BookListByCategoryActivity.class);
        intent.putExtra("category", categoryName);
        startActivity(intent);
    }
}