package com.example.oo_frontend.UI.main.book;

import static com.example.oo_frontend.Network.RetrofitHelper.fetchBooksByDepartment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.chat.list.ChatListActivity;
import com.example.oo_frontend.UI.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.oo_frontend.UI.mypage.MyPageActivity;

import java.util.ArrayList;
import java.util.List;

public class BookListByCategoryActivity extends AppCompatActivity {

    private RecyclerView bookRecyclerView;
    private com.example.oo_frontend.UI.main.book.BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    private EditText searchInput;
    private Button searchByTitleButton, searchByProfessorButton;

    private enum SearchType { TITLE, PROFESSOR, NONE }
    private SearchType selectedType = SearchType.NONE;

    private String categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_by_category);

        // ⛳ 카테고리 받기
        categoryName = getIntent().getStringExtra("category");
        TextView pageTitle = findViewById(R.id.pageTitle);
        if (categoryName != null && !categoryName.isEmpty()) {
            pageTitle.setText(categoryName);
        }

        // XML 연결
        bookRecyclerView = findViewById(R.id.bookRecyclerView);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchInput = findViewById(R.id.searchInput);
        searchByTitleButton = findViewById(R.id.searchByTitleButton);
        searchByProfessorButton = findViewById(R.id.searchByProfessorButton);

        fetchBooksByDepartment(this, categoryName, new ApiCallback<List<Book>>() {
            @Override
            public void onSuccess(List<Book> response) {
                bookList.clear();
                bookList.addAll(response);
                bookAdapter = new BookAdapter(bookList, BookListByCategoryActivity.this);
                bookRecyclerView.setAdapter(bookAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(BookListByCategoryActivity.this, "서버 오류: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        // 버튼 클릭 → 검색 기준 설정
        searchByTitleButton.setOnClickListener(v -> {
            selectedType = SearchType.TITLE;
            highlightSelectedTab(searchByTitleButton);
        });

        searchByProfessorButton.setOnClickListener(v -> {
            selectedType = SearchType.PROFESSOR;
            highlightSelectedTab(searchByProfessorButton);
        });

        // 입력 시 필터링
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (selectedType == SearchType.NONE) {
                    Toast.makeText(BookListByCategoryActivity.this, "검색 기준을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                bookAdapter.filter(s.toString(), selectedType == SearchType.TITLE);
            }
        });

        // 하단 네비게이션 바
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_home); // 현재 위치 표시

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                Intent intent = new Intent(this, ChatListActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(this, MyPageActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }



    private void highlightSelectedTab(Button selected) {
        Button[] allTabs = {searchByTitleButton, searchByProfessorButton};
        for (Button btn : allTabs) {
            btn.setBackgroundResource(R.drawable.tab_unselected);
        }
        selected.setBackgroundResource(R.drawable.tab_selected);
    }
}
