package com.example.oo_frontend.UI.main.book;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.chat.room.ChatActivity;
import com.example.oo_frontend.UI.chat.list.ChatListActivity;
import com.example.oo_frontend.UI.main.MainActivity;
import com.example.oo_frontend.UI.mypage.MyPageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView mainImage, likeIcon;
    private TextView bookTitleView, professorTag, categoryTag, salePriceView, discountRateView, bookDescriptionView, originalPriceView;
    private ImageButton backButton, reportButton;
    private Button inquiryButton;

    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.white);
        setContentView(R.layout.activity_book_detail);

        bindViews();

        // 1. intent로 productId 받기
        long productId = getIntent().getLongExtra("productId", -1);
        if (productId == -1) {
            Toast.makeText(this, "교재 ID가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. API 호출로 상세정보 받아오기
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
        RetrofitHelper.getBookDetail(this, productId, new ApiCallback<Book>() {
            @Override
            public void onSuccess(Book book) {
                bindBookToUI(book);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(BookDetailActivity.this, "불러오기 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindBookToUI(Book book) {
        bookTitleView.setText(book.getTitle());
        professorTag.setText(book.getProfessorName());
        categoryTag.setText(book.getCategory());
        salePriceView.setText(book.getPrice() + "원");
        bookDescriptionView.setText(book.getDescription());

        if (book.getOfficialPrice() > 0) {
            originalPriceView.setText("정가 " + book.getOfficialPrice() + "원");
            int discount = Math.round((book.getOfficialPrice() - book.getPrice()) * 100f / book.getOfficialPrice());
            discountRateView.setText(discount + "%");
        } else {
            discountRateView.setText("");
        }

        // 이미지 URI 표시
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            mainImage.setImageURI(Uri.parse(book.getImageUrl()));
            // TODO: Glide 사용 권장
        }

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
            });

            inquiryButton.setOnClickListener(v -> {
                Intent chatIntent = new Intent(this, ChatActivity.class);
                chatIntent.putExtra("sellerName", "이기연"); // TODO: sellerName 받아올 것
                chatIntent.putExtra("bookTitle", book.getTitle());
                chatIntent.putExtra("bookPrice", String.valueOf(book.getPrice()));
                chatIntent.putExtra("bookImageResId", R.drawable.book_sample1); // TODO: Glide 처리
                startActivity(chatIntent);
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
        mainImage = findViewById(R.id.bookMainImage);
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
    }
}


