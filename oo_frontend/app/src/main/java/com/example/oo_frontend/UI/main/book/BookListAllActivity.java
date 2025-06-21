package com.example.oo_frontend.UI.main.book;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.Network.ApiCallback;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oo_frontend.Model.SearchResultDto;
import com.example.oo_frontend.UI.mypage.MyPageActivity;
import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Network.*;
import com.example.oo_frontend.Network.RetrofitService;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.chat.list.ChatListActivity;
import com.example.oo_frontend.UI.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListAllActivity extends AppCompatActivity {

    private RecyclerView bookRecyclerView;
    private com.example.oo_frontend.UI.main.book.BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    private EditText searchInput;
    private Button searchByTitleButton, searchByProfessorButton;
    private LinearLayout recentSearchContainer;

    private enum SearchType { TITLE, PROFESSOR, NONE }
    private SearchType selectedType = SearchType.NONE;

    private static final String PREF_RECENT = "recent_search";
    private static final String PREF_KEY = "keywords";

    private RetrofitService retrofitService;
    private Long userId;

    private void handleSearchResponse(Response<List<SearchResultDto>> response, String keyword) {
        if (response.isSuccessful() && response.body() != null) {
            List<SearchResultDto> results = response.body();
            if (results.isEmpty()) {
                Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                bookList.clear();
                for (SearchResultDto dto : results) {
                    bookList.add(dto.toBook());
                }
                bookAdapter.notifyDataSetChanged();
                saveRecentKeyword(keyword);
            }
        } else {
            Toast.makeText(this, "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // SharedPreferences에서 userId 가져오기
        SharedPreferences prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        userId = (long) prefs.getInt("userId", -1); // int로 저장했기 때문에 long으로 변환
        if (userId == null || userId == -1L) {
            Toast.makeText(BookListAllActivity.this, "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrofit 객체 초기화
        retrofitService = RetrofitHelper.getApiService();


        bookRecyclerView = findViewById(R.id.bookRecyclerView);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookAdapter = new com.example.oo_frontend.UI.main.book.BookAdapter(bookList, this);
        bookRecyclerView.setAdapter(bookAdapter);

        // 실제 서버에서 데이터 받아오기
        getAllBooksFromServer();

        // 🔗 XML 연결
        searchInput = findViewById(R.id.searchInput);
        searchByTitleButton = findViewById(R.id.searchByTitleButton);
        searchByProfessorButton = findViewById(R.id.searchByProfessorButton);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // 🔍 검색어 입력 감지
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();

                if (selectedType == SearchType.NONE) {
                    Toast.makeText(BookListAllActivity.this, "검색 기준을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (keyword.isEmpty()) {
                    getAllBooksFromServer();  // 비어있으면 전체 목록 다시 불러오기
                    return;
                }

                if (userId == null || userId == -1L) {
                    Toast.makeText(BookListAllActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String filter = selectedType == SearchType.TITLE ? "title" : "professor";

                if ("title".equals(filter)) {
                    retrofitService.searchByTitle(keyword)
                            .enqueue(new Callback<List<Book>>() {
                                @Override
                                public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                                    bookAdapter.updateBooks(response.body());
                                }

                                @Override
                                public void onFailure(Call<List<Book>> call, Throwable t) {
                                    Toast.makeText(BookListAllActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    retrofitService.searchByProfessor(keyword)
                            .enqueue(new Callback<List<Book>>() {
                                @Override
                                public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                                    bookAdapter.updateBooks(response.body());
                                }

                                @Override
                                public void onFailure(Call<List<Book>> call, Throwable t) {
                                    Toast.makeText(BookListAllActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }


            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 🔘 검색 기준 선택
        searchByTitleButton.setOnClickListener(v -> {
            selectedType = SearchType.TITLE;
            highlightSelectedTab(searchByTitleButton);
        });

        searchByProfessorButton.setOnClickListener(v -> {
            selectedType = SearchType.PROFESSOR;
            highlightSelectedTab(searchByProfessorButton);
        });



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

    // ✅ 서버에서 교재 목록 받아오기 (fetchAllBooks 사용)
    private void getAllBooksFromServer() {
        RetrofitHelper.fetchAllBooks(this, new ApiCallback<List<Book>>() {
            @Override
            public void onSuccess(List<Book> response) {
                Log.d("BookList", "받은 교재 수: " + response.size());
                bookAdapter.updateBooks(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(BookListAllActivity.this, "서버 오류: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void highlightSelectedTab(Button selected) {
        Button[] allTabs = {searchByTitleButton, searchByProfessorButton};
        for (Button btn : allTabs) {
            btn.setBackgroundResource(R.drawable.tab_unselected);
        }
        selected.setBackgroundResource(R.drawable.tab_selected);
    }

    private void saveRecentKeyword(String keyword) {
        SharedPreferences prefs = getSharedPreferences(PREF_RECENT, Context.MODE_PRIVATE);
        Set<String> recentSet = prefs.getStringSet(PREF_KEY, new LinkedHashSet<>());

        Set<String> newSet = new LinkedHashSet<>(recentSet);
        newSet.add(keyword);

        prefs.edit().putStringSet(PREF_KEY, newSet).apply();
    }
}
