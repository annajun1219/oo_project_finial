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
    private SalesAdapter salesAdapter;
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
        Button btnSetSelling = findViewById(R.id.btn_set_selling);       // 상태 변경용
        Button btnSetReserved = findViewById(R.id.btn_set_reserved);     // 상태 변경용
        Button btnSetSold = findViewById(R.id.btn_set_sold);             // 상태 변경용
        Button btnChangeMode = findViewById(R.id.btn_change_status);
        LinearLayout layoutStatusButtons = findViewById(R.id.layout_status_buttons);

        // ✅ 필터 버튼 (상태별 조회용)
        Button btnAll = findViewById(R.id.btn_all);
        Button btnFilterSelling = findViewById(R.id.btn_selling);
        Button btnFilterReserved = findViewById(R.id.btn_reserved);
        Button btnFilterSold = findViewById(R.id.btn_sold);

        // ✅ SharedPreferences에서 userId 가져오기
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "userId가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ 처음 앱 시작 시 전체 조회
        loadSalesByStatus("전체");

        // ✅ 필터 버튼 클릭 시 상태별 조회
        btnAll.setOnClickListener(v -> loadSalesByStatus("전체"));
        btnFilterSelling.setOnClickListener(v -> loadSalesByStatus("판매중"));
        btnFilterReserved.setOnClickListener(v -> loadSalesByStatus("예약중"));
        btnFilterSold.setOnClickListener(v -> loadSalesByStatus("판매완료"));

        // ✅ 상태 변경 모드 전환
        btnChangeMode.setOnClickListener(v -> {
            if (salesAdapter != null) {
                salesAdapter.toggleCheckboxVisibility();
                layoutStatusButtons.setVisibility(View.VISIBLE);
            }
        });

        // ✅ 선택된 아이템 상태 변경
        btnSetSelling.setOnClickListener(v -> updateSelectedItemsStatus("판매중"));
        btnSetReserved.setOnClickListener(v -> updateSelectedItemsStatus("예약중"));
        btnSetSold.setOnClickListener(v -> updateSelectedItemsStatus("판매완료"));
    }

    // 🔄 상태에 따라 판매 목록 조회
    private void loadSalesByStatus(String status) {
        RetrofitHelper.getSales(this, (long) userId, status, new ApiCallback<List<SaleItem>>() {
            @Override
            public void onSuccess(List<SaleItem> data) {
                salesList = data;
                salesAdapter = new SalesAdapter(salesList);
                recyclerView.setAdapter(salesAdapter);
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(SalesHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ 선택된 항목들의 상태 변경
    private void updateSelectedItemsStatus(String newStatus) {
        for (SaleItem item : salesList) {
            if (item.isSelected) {
                // 판매중은 bookId로, 그 외는 transactionId로 상태 업데이트
                Long idToUse = newStatus.equals("판매중") ? item.bookId : item.transactionId;

                RetrofitHelper.updateSaleStatus(
                        SalesHistoryActivity.this,
                        (long) userId,
                        idToUse,
                        newStatus,
                        new ApiCallback<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                item.status = newStatus;
                                item.isSelected = false;
                                runOnUiThread(() -> salesAdapter.notifyDataSetChanged());
                            }

                            @Override
                            public void onFailure(String msg) {
                                Toast.makeText(SalesHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        }
    }
}
