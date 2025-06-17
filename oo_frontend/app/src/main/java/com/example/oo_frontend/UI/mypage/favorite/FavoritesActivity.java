package com.example.oo_frontend.UI.mypage.favorite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oo_frontend.Model.FavoriteItem;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    com.example.oo_frontend.UI.mypage.favorite.FavoriteAdapter adapter;
    List<FavoriteItem> favoriteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new com.example.oo_frontend.UI.mypage.favorite.FavoriteAdapter(this, favoriteList);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadFavoriteListFromServer(); // ✅ 서버에서 불러옴
    }

    private void loadFavoriteListFromServer() {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) return;

        Log.d("찜목록", "찜목록 불러오기 시작. userId: " + userId); // 로그 추가

        RetrofitHelper.getFavorites(this, userId, new ApiCallback<List<FavoriteItem>>() {
            @Override
            public void onSuccess(List<FavoriteItem> data) {
                favoriteList.clear();
                favoriteList.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(FavoritesActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
