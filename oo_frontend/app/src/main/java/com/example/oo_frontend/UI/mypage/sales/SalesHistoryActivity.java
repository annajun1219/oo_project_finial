package com.example.oo_frontend.UI.mypage.sales;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oo_frontend.Model.SaleItem;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.R;

import java.util.ArrayList;
import java.util.List;

public class SalesHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private com.example.oo_frontend.UI.mypage.sales.SalesAdapter salesAdapter;
    private List<SaleItem> salesList = new ArrayList<>();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        // ✅ RecyclerView 초기화
        recyclerView = findViewById(R.id.recycler_sales);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ 버튼 바인딩
        Button btnSetSelling = findViewById(R.id.btn_set_selling);
        Button btnSetReserved = findViewById(R.id.btn_set_reserved);
        Button btnSetSold = findViewById(R.id.btn_set_sold);
        Button btnChangeMode = findViewById(R.id.btn_change_status);
        LinearLayout layoutStatusButtons = findViewById(R.id.layout_status_buttons);

        // ✅ 사용자 userId 가져오기 (SharedPreferences)
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "userId가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ 서버에서 판매내역 불러오기
        RetrofitHelper.getSales(this, userId, new ApiCallback<List<SaleItem>>() {
            @Override
            public void onSuccess(List<SaleItem> data) {
                salesList = data;
                salesAdapter = new com.example.oo_frontend.UI.mypage.sales.SalesAdapter(salesList);
                recyclerView.setAdapter(salesAdapter);
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(SalesHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ 판매 상태 변경 모드로 전환
        btnChangeMode.setOnClickListener(v -> {
            if (salesAdapter != null) {
                salesAdapter.toggleCheckboxVisibility(); // 체크박스 보이기
                layoutStatusButtons.setVisibility(View.VISIBLE); // 상태 변경 버튼 보이기
            }
        });

        // ✅ 각 상태 버튼 클릭 시 선택된 아이템 상태 변경
        btnSetSelling.setOnClickListener(v -> updateSelectedItemsStatus("판매중"));
        btnSetReserved.setOnClickListener(v -> updateSelectedItemsStatus("예약중"));
        btnSetSold.setOnClickListener(v -> updateSelectedItemsStatus("판매완료"));
    }

    // ✅ 선택된 아이템들의 거래 상태를 서버에 PATCH 요청
    private void updateSelectedItemsStatus(String newStatus) {
        for (SaleItem item : salesList) {
            if (item.isSelected) {
                RetrofitHelper.updateSaleStatus(this, userId, item.bookId, newStatus, new ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        item.status = newStatus;
                        item.isSelected = false;
                        salesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String msg) {
                        Toast.makeText(SalesHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
