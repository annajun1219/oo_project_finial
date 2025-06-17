package com.example.oo_frontend.UI.main.recommendation;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;

import java.util.List;

public class BookRecommendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout dotIndicator;
    private int userId = 1;
    private int totalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_recommend);

        userId = getIntent().getIntExtra("userId", 1);
        recyclerView = findViewById(R.id.recyclerRecommend);
        dotIndicator = findViewById(R.id.dotIndicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // 백엔드에서 추천 도서 목록 불러오기
        RetrofitHelper.fetchRecommendations(this, (long) userId, new ApiCallback<List<Book>>() {
            @Override
            public void onSuccess(List<Book> data) {
                RecommendationAdapter adapter = new RecommendationAdapter(data);
                recyclerView.setAdapter(adapter);

                totalPages = (int) Math.ceil(data.size() / 2.0); // 페이지 수 계산 (2개씩 표시)
                setupIndicators(totalPages);
                updateIndicators(0); // 첫 페이지 표시

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        int firstVisible = layoutManager.findFirstVisibleItemPosition();
                        int currentPage = firstVisible / 2; // 페이지 인덱스 계산
                        updateIndicators(currentPage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(BookRecommendActivity.this, "추천 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 점 ●●● 생성
    private void setupIndicators(int count) {
        dotIndicator.removeAllViews();
        for (int i = 0; i < count; i++) {
            TextView dot = new TextView(this);
            dot.setText("●");
            dot.setTextSize(16);
            dot.setPadding(6, 0, 6, 0);
            dot.setTextColor(Color.LTGRAY);
            dotIndicator.addView(dot);
        }
    }

    // 현재 페이지 위치에 따라 점 색 변경
    private void updateIndicators(int position) {
        for (int i = 0; i < dotIndicator.getChildCount(); i++) {
            TextView dot = (TextView) dotIndicator.getChildAt(i);
            dot.setTextColor(i == position ? Color.BLACK : Color.LTGRAY);
        }
    }
}