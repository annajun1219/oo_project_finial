package com.example.oo_frontend.UI.mypage.buy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.oo_frontend.Model.PurchaseItem;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuyHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private com.example.oo_frontend.UI.mypage.buy.BuyAdapter adapter;
    private List<PurchaseItem> allItems = new ArrayList<>();

    private Button btnAll, btnSelling, btnSold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_history);

        recyclerView = findViewById(R.id.recyclerBuyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAll = findViewById(R.id.btn_all);
        btnSelling = findViewById(R.id.btn_selling);
        btnSold = findViewById(R.id.btn_sold);

        adapter = new com.example.oo_frontend.  UI.mypage.buy.BuyAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        loadBuyHistory();

        btnAll.setOnClickListener(v -> filterList("ALL"));
        btnSelling.setOnClickListener(v -> filterList("예약 중"));
        btnSold.setOnClickListener(v -> filterList("판매 완료"));
    }

    private void loadBuyHistory() {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "userId가 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ 헬퍼에서 바로 List<PurchaseItem> 받아오기
        RetrofitHelper.getPurchaseHistory(this, (long) userId, new ApiCallback<List<PurchaseItem>>() {
            @Override
            public void onSuccess(List<PurchaseItem> purchases) {
                allItems = purchases;
                adapter.updateList(allItems);
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(BuyHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterList(String status) {
        if (status.equals("ALL")) {
            adapter.updateList(allItems);
            return;
        }

        List<PurchaseItem> filtered = new ArrayList<>();
        for (PurchaseItem item : allItems) {
            if (item.status.equals(status)) {
                filtered.add(item);
            }
        }
        adapter.updateList(filtered);
    }
}