package com.example.oo_frontend.UI.mypage.review;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.ReviewItem;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;
import java.util.*;

public class ReviewActivity extends AppCompatActivity {

    private ImageView imageProfile;
    private TextView textNickname, textAverageRating, textReviewCount;
    private RatingBar ratingBar;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter adapter;

    private List<ReviewItem> reviewList = new ArrayList<>();
    private Map<String, Integer> keywordCountMap = new HashMap<>();

    private int sellerId = 1;    // 예시용 - 나중엔 Intent 등으로 받는 게 이상적
    private int productId = 88;  // 예시용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // ✅ 뷰 초기화
        imageProfile = findViewById(R.id.imageProfile);
        textNickname = findViewById(R.id.textNickname);
        textAverageRating = findViewById(R.id.textAverageRating);
        textReviewCount = findViewById(R.id.textReviewCount);
        ratingBar = findViewById(R.id.ratingBar);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        // ✅ RecyclerView 설정
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setAdapter(adapter);

        loadUserProfile(); // 사용자 정보 표시
        loadReviews();     // 리뷰 목록 불러오기
    }

    // ✅ 유저 닉네임 / 프로필 이미지를 표시
    private void loadUserProfile() {
        // 예시 하드코딩 (추후 SharedPreferences or Intent 로 수정 가능)
        String nickname = "책좋아";
        String profileImage = "https://cdn.site/img.jpg";

        textNickname.setText(nickname);
        Glide.with(this).load(profileImage).circleCrop().into(imageProfile);
    }

    // ✅ 서버에서 리뷰 목록 불러오기
    private void loadReviews() {
        RetrofitHelper.getReviews(this, productId, sellerId, new ApiCallback<List<ReviewItem>>() {
            @Override
            public void onSuccess(List<ReviewItem> reviews) {
                reviewList.clear();
                reviewList.addAll(reviews);
                adapter.notifyDataSetChanged();
                updateStats();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(ReviewActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ 별점 평균, 후기 수 계산
    private void updateStats() {
        float totalRating = 0;
        keywordCountMap.clear();

        for (ReviewItem item : reviewList) {
            totalRating += item.rating;

            if (item.keywords != null) {
                for (String keyword : item.keywords) {
                    int count = keywordCountMap.containsKey(keyword) ? keywordCountMap.get(keyword) : 0;
                    keywordCountMap.put(keyword, count + 1);
                }
            }
        }

        float avgRating = reviewList.isEmpty() ? 0 : totalRating / reviewList.size();
        ratingBar.setRating(avgRating);
        textAverageRating.setText(String.format("%.2f", avgRating));
        textReviewCount.setText("후기 " + reviewList.size());

        updateKeywordUI();
    }

    // ✅ 상단 키워드 카운트 UI 반영
    private void updateKeywordUI() {
        // 미리 정의된 키워드 ID들과 텍스트 키워드 매핑
        Map<Integer, Integer> keywordIds = new HashMap<>();
        keywordIds.put(1, R.id.keyword1_count);
        keywordIds.put(2, R.id.keyword2_count);
        keywordIds.put(3, R.id.keyword3_count);

        List<String> targetKeywords = Arrays.asList(
                "답장이 빨라요",
                "교재설명과 실제상품이 동일해요",
                "친절하고 배려가 넘쳐요"
        );

        for (int i = 0; i < targetKeywords.size(); i++) {
            String keyword = targetKeywords.get(i);
            int count = keywordCountMap.containsKey(keyword) ? keywordCountMap.get(keyword) : 0;
            TextView countView = findViewById(keywordIds.get(i + 1));
            countView.setText(String.valueOf(count));
        }
    }
}
