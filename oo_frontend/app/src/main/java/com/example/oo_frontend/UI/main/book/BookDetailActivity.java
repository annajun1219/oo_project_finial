package com.example.oo_frontend.UI.main.book;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Model.ChatRoom;
import com.example.oo_frontend.Model.StartChatRequestDto;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitClient;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.Network.RetrofitService;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.chat.room.ChatActivity;
import com.example.oo_frontend.UI.chat.list.ChatListActivity;
import com.example.oo_frontend.UI.main.MainActivity;
import com.example.oo_frontend.UI.mypage.MyPageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView mainImage, likeIcon;
    private TextView bookTitleView, professorTag, categoryTag, salePriceView,
            discountRateView, bookDescriptionView, originalPriceView, sellerNameView;;
    private ImageButton backButton, reportButton;
    private Button inquiryButton;

    private TextView averagePriceView;

    private final RetrofitService retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
    private Book currentBook;
    private long userId;


    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.white);
        setContentView(R.layout.activity_book_detail);

        bindViews();

        // 1. intent로 productId 받기
        long productId = getIntent().getLongExtra("productId", -1);
        Log.d("BookDetail", "📦 전달받은 productId: " + productId);
        if (productId == -1) {
            Toast.makeText(this, "교재 ID가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. API 호출로 상세정보 받아오기
        userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getLong("userId", -1);
        fetchBookDetail(productId);

        // 3. 뒤로가기 버튼
        backButton.setOnClickListener(v -> finish());

        // 4. 하단 네비게이션 바
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatListActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, MyPageActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void fetchBookDetail(long productId) {
        long viewerId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getLong("userId", -1); // 예시: 로그인한 유저 ID

        RetrofitHelper.getBookDetail(this, productId, viewerId, new ApiCallback<Book>() {
            @Override
            public void onSuccess(Book book) {
                bindBookToUI(book);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("DEBUG", "❌ API 실패: " + errorMessage);
            }
        });
    }


    private void bindBookToUI(Book book) {
        bookTitleView.setText(book.getTitle());
        Log.d("BookDetail", "📚 교수명: " + book.getProfessorName());
        Log.d("BookDetail", "📚 카테고리: " + book.getCategory());

        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = (long) prefs.getInt("userId", -1);

        professorTag.setText(book.getProfessorName());
        categoryTag.setText(book.getCategory());
        salePriceView.setText(book.getPrice() + "원");
        bookDescriptionView.setText(book.getDescription());

        // 시세 불러오기
        RetrofitHelper.getAveragePrice(BookDetailActivity.this, book.getTitle(), new ApiCallback<Double>() {
            @Override
            public void onSuccess(Double avgPrice) {
                if (avgPrice == null || avgPrice == 0.0) {
                    // 등록된 책이 하나뿐이거나 시세 없음
                    averagePriceView.setText("시세: " + book.getPrice() + "원");
                } else {
                    averagePriceView.setText("시세: " + avgPrice.intValue() + "원");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // 네트워크 오류 등 → 현재 책 가격으로 대체
                averagePriceView.setText("시세: " + book.getPrice() + "원");
                Log.e("BookDetail", "❌ 시세 API 실패: " + errorMessage);
            }
        });

        // AFTER
        if (book.getSeller() != null && book.getSeller().getName() != null) {
            sellerNameView.setText(book.getSeller().getName());
        } else {
            sellerNameView.setText("판매자 정보 없음");
        }


        if (book.getOfficialPrice() > 0) {
            originalPriceView.setText("정가 " + book.getOfficialPrice() + "원");
            int discount = Math.round((book.getOfficialPrice() - book.getPrice()) * 100f / book.getOfficialPrice());
            discountRateView.setText(discount + "%");
        } else {
            discountRateView.setText("");
        }

        Glide.with(this)
                .load(book.getImageUrl())  // ✅ 이미 완성된 URL이라 그대로 사용해야 함
                .into(mainImage);



        // ✨ 구매자 / 판매자 구분
        if (book.isMyPost()) {
            likeIcon.setVisibility(ImageView.GONE);
            inquiryButton.setVisibility(Button.GONE);
            reportButton.setVisibility(ImageButton.GONE);
        } else {
            likeIcon.setOnClickListener(v -> {
                isLiked = !isLiked;
                likeIcon.setImageResource(isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
                Toast.makeText(this, isLiked ? "찜 추가" : "찜 해제", Toast.LENGTH_SHORT).show();

                Long bookId = book.getProductId();
                if (bookId == null || bookId == -1) {
                    bookId = getIntent().getLongExtra("productId", -1);
                }

                Log.d("찜추가", "📌 userId: " + userId + ", bookId: " + bookId);

                if (userId == -1 || bookId == -1) {
                    Toast.makeText(this, "로그인 또는 책 정보가 필요합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isLiked) {
                    retrofitService.addFavorite(bookId, userId).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            // 응답 코드와 바디를 로그로 찍어 응답 상태 확인
                            Log.d("찜응답", "✅ code: " + response.code());
                            Log.d("찜응답", "✅ body: " + response.body());
                            Log.d("찜응답", "✅ errorBody: " + (response.errorBody() != null ? response.errorBody().toString() : "없음"));

                            if (response.isSuccessful()) {
                                Log.d("찜응답", "찜 등록 성공");
                            } else {
                                // 실패 시 에러 메시지 처리
                                Log.e("찜응답", "찜 등록 실패: " + response.code());
                                Toast.makeText(BookDetailActivity.this, "찜 등록 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            // 네트워크 오류 발생 시 로그
                            Log.e("찜응답", "서버 오류: " + t.getMessage());
                            Toast.makeText(BookDetailActivity.this, "서버 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    retrofitService.deleteFavorite(bookId, userId).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            // 응답 코드와 바디를 로그로 찍어 응답 상태 확인
                            Log.d("찜응답", "✅ code: " + response.code());
                            Log.d("찜응답", "✅ body: " + response.body());
                            Log.d("찜응답", "✅ errorBody: " + (response.errorBody() != null ? response.errorBody().toString() : "없음"));

                            if (response.isSuccessful()) {
                                Log.d("찜응답", "찜 해제 성공");
                            } else {
                                // 실패 시 에러 메시지 처리
                                Log.e("찜응답", "찜 해제 실패: " + response.code());
                                Toast.makeText(BookDetailActivity.this, "찜 해제 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            // 네트워크 오류 발생 시 로그
                            Log.e("찜응답", "서버 오류: " + t.getMessage());
                            Toast.makeText(BookDetailActivity.this, "서버 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            });



            inquiryButton.setOnClickListener(v -> {
                StartChatRequestDto requestDto = new StartChatRequestDto(userId, book.getSeller().getSellerId(), book.getProductId());

                retrofitService.startChatRoom(requestDto).enqueue(new Callback<ChatRoom>() {
                    @Override
                    public void onResponse(Call<ChatRoom> call, Response<ChatRoom> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            long roomId = response.body().getRoomId();
                            Intent chatIntent = new Intent(BookDetailActivity.this, ChatActivity.class);
                            chatIntent.putExtra("bookId", book.getProductId());
                            chatIntent.putExtra("roomId", roomId);
                            chatIntent.putExtra("userId", userId);
                            chatIntent.putExtra("sellerName", book.getSeller().getName());
                            chatIntent.putExtra("bookTitle", book.getTitle());
                            chatIntent.putExtra("bookPrice", String.valueOf(book.getPrice()));
                            chatIntent.putExtra("bookImageUrl", book.getImageUrl());
                            startActivity(chatIntent);
                        } else {
                            Toast.makeText(BookDetailActivity.this, "채팅방을 시작할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatRoom> call, Throwable t) {
                        Toast.makeText(BookDetailActivity.this, "네트워크 오류로 채팅 시작 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            });




            reportButton.setOnClickListener(v -> showReportDialog(book));
        }
    }


    private void showReportDialog(Book book) {
        String[] reasons = {"비방 및 욕설", "부적절한 사진", "무통보 거래 파기", "기타"};
        final int[] selected = {-1};

        new AlertDialog.Builder(this)
                .setTitle("신고 사유를 선택해주세요")
                .setSingleChoiceItems(reasons, -1, (dialog, which) -> selected[0] = which)
                .setPositiveButton("신고하기", (dialog, which) -> {
                    if (selected[0] == -1) {
                        Toast.makeText(this, "신고 사유를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        String reason = reasons[selected[0]];
                        Toast.makeText(this, "신고가 접수되었습니다: " + reason, Toast.LENGTH_SHORT).show();
                        // TODO: POST /api/reports 전송 구현
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void bindViews() {
        mainImage = findViewById(R.id.mainImage);  // XML에 맞춰 ID 수정
        likeIcon = findViewById(R.id.bookLikeIcon);
        backButton = findViewById(R.id.backButton);
        inquiryButton = findViewById(R.id.inquiryButton);
        reportButton = findViewById(R.id.reportButton);
        bookTitleView = findViewById(R.id.bookTitle);
        professorTag = findViewById(R.id.professorTag);
        categoryTag = findViewById(R.id.categoryTag);
        salePriceView = findViewById(R.id.salePrice);
        discountRateView = findViewById(R.id.discountRate);
        bookDescriptionView = findViewById(R.id.bookDescription);
        originalPriceView = findViewById(R.id.textOriginalPrice);
        sellerNameView = findViewById(R.id.sellerName);
        averagePriceView = findViewById(R.id.averagePriceView);

    }
}


