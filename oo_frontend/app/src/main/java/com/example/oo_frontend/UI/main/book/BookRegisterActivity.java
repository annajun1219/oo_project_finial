package com.example.oo_frontend.UI.main.book;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Model.BookRegisterResponse;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BookRegisterActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_REQUEST = 101;
    private static final int MAX_IMAGE_COUNT = 5;

    private ArrayList<Uri> imageUris = new ArrayList<>();
    private LinearLayout imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_register);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        String[] categories = {"문과대학", "이과대학", "공과대학", "생활과학", "사회과학", "법과대학",
                "경상대학", "음악대학", "약학대학", "미술대학", "교양과목", "자격증"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerCategory.setAdapter(adapter);

        imageContainer = findViewById(R.id.imageContainer);
        ImageView imageUploadButton = findViewById(R.id.imageUploadButton);
        imageUploadButton.setOnClickListener(v -> {
            if (imageUris.size() >= MAX_IMAGE_COUNT) {
                Toast.makeText(this, "이미지는 최대 5장까지 업로드할 수 있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_REQUEST);
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            String title = ((EditText) findViewById(R.id.editTitle)).getText().toString();
            String professor = ((EditText) findViewById(R.id.editProfessor)).getText().toString();
            String officialPriceStr = ((EditText) findViewById(R.id.editOfficialPrice)).getText().toString();
            String priceStr = ((EditText) findViewById(R.id.editPrice)).getText().toString();
            String description = ((EditText) findViewById(R.id.editDescription)).getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (title.isEmpty() || officialPriceStr.isEmpty() || priceStr.isEmpty() || description.isEmpty() || imageUris.isEmpty()) {
                Toast.makeText(this, "모든 필드를 채우고 이미지를 최소 1장 업로드해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            int officialPrice, price;
            try {
                officialPrice = Integer.parseInt(officialPriceStr);
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "가격 입력이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            File imageFile;
            try {
                imageFile = RetrofitHelper.getFileFromUri(this, imageUris.get(0));
            } catch (IOException e) {
                Toast.makeText(this, "이미지 파일 변환 오류", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ 서버에 저장된 이미지 이름과 동일한 URL 생성
            String imageUrl = "http://10.0.2.2:8080/public/" + imageFile.getName();

            long sellerId = getSharedPreferences("loginPrefs", MODE_PRIVATE).getInt("userId", -1);
            if (sellerId == -1) {
                Toast.makeText(this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            RetrofitHelper.registerBook(
                    this,
                    title,
                    professor,
                    officialPrice,
                    price,
                    description,
                    category,
                    sellerId,
                    imageUrl,
                    new ApiCallback<BookRegisterResponse>() {
                        @Override
                        public void onSuccess(BookRegisterResponse response) {
                            Long productId = response.getBookId();
                            Intent intent = new Intent(BookRegisterActivity.this, BookDetailActivity.class);
                            intent.putExtra("productId", productId);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(BookRegisterActivity.this, "등록 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                imageUris.add(imageUri);

                ImageView preview = new ImageView(this);
                preview.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
                preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                preview.setImageURI(imageUri);

                imageContainer.addView(preview);
            }
        }
    }
}
