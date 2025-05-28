package com.example.oo_frontend.UI.main.recommendation;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oo_frontend.Model.Recommendation;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.R;

import java.util.List;

public class BookRecommendActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout dotIndicator;
    private int userId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_recommend);

        userId = getIntent().getIntExtra("userId", 1);
        viewPager = findViewById(R.id.viewPager);
        dotIndicator = findViewById(R.id.dotIndicator);

        // 카드 넘김 애니메이션 효과
        viewPager.setPageTransformer((page, position) -> {
            float scale = 1 - Math.abs(position) * 0.1f;
            page.setScaleY(scale);
        });

        // 페이지 바뀔 때 인디케이터 업데이트
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);
            }
        });

        // ✅ 백엔드로부터 추천 데이터 불러오기
        RetrofitHelper.fetchRecommendations(this, userId, new ApiCallback<List<Recommendation>>() {
            @Override
            public void onSuccess(List<Recommendation> data) {
                RecommendationAdapter adapter = new RecommendationAdapter(data, viewPager);
                viewPager.setAdapter(adapter);
                setupIndicators(data.size());
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(BookRecommendActivity.this, "추천 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ●●● 점 생성
    private void setupIndicators(int size) {
        dotIndicator.removeAllViews();
        for (int i = 0; i < size; i++) {
            TextView dot = new TextView(this);
            dot.setText("●");
            dot.setTextSize(16);
            dot.setPadding(6, 0, 6, 0);
            dot.setTextColor(i == 0 ? Color.BLACK : Color.LTGRAY);
            dotIndicator.addView(dot);
        }
    }

    // ● 점 색상 바꾸기
    private void updateIndicators(int position) {
        for (int i = 0; i < dotIndicator.getChildCount(); i++) {
            TextView dot = (TextView) dotIndicator.getChildAt(i);
            dot.setTextColor(i == position ? Color.BLACK : Color.LTGRAY);
        }
    }
}
