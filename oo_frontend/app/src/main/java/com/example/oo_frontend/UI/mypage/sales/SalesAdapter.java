package com.example.oo_frontend.UI.mypage.sales;

import static com.example.oo_frontend.Network.RetrofitHelper.updateSaleStatus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.SaleItem;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.R;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder> {

    private List<SaleItem> salesList;
    private boolean showCheckboxes = false;

    public SalesAdapter(List<SaleItem> salesList) {
        this.salesList = salesList;
    }

    public void toggleCheckboxVisibility() {
        showCheckboxes = !showCheckboxes;
        notifyDataSetChanged();
    }

    public void updateStatusForSelectedItems(String newStatus) {
        for (SaleItem item : salesList) {
            if (item.isSelected) {
                item.status = newStatus;
                item.isSelected = false;
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sales, parent, false);
        return new SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        SaleItem item = salesList.get(position);

        holder.tvDate.setText(item.createdAt);
        holder.tvTitle.setText(item.title);
        holder.tvPrice.setText(String.format("%,d원", item.price));
        holder.btnStatus.setText(item.status);

        if (item.imageUrl != null && !item.imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.imageUrl)
                    .placeholder(R.drawable.sample_book)
                    .into(holder.ivBook);
        } else {
            holder.ivBook.setImageResource(R.drawable.sample_book);
        }


        switch (item.status) {
            case "판매중":
                holder.btnStatus.setBackgroundResource(R.drawable.status_sale);
                holder.btnStatus.setTextColor(Color.BLACK);
                break;
            case "예약중":
                holder.btnStatus.setBackgroundResource(R.drawable.status_reserved);
                holder.btnStatus.setTextColor(Color.BLACK);
                break;
            case "판매완료":
                holder.btnStatus.setBackgroundResource(R.drawable.status_done);
                holder.btnStatus.setTextColor(Color.WHITE);
                break;
        }

        holder.checkbox.setVisibility(showCheckboxes ? View.VISIBLE : View.GONE);
        holder.checkbox.setChecked(item.isSelected);
        holder.checkbox.setOnCheckedChangeListener((btnView, isChecked) -> item.isSelected = isChecked);

        holder.btnStatus.setOnClickListener(v -> {
            if (!item.status.equals("판매완료")) {
                // SharedPreferences에서 userId 가져오기
                SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                long userId = prefs.getInt("userId", -1);

                if (userId == -1) {
                    Toast.makeText(holder.itemView.getContext(), "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // transactionId 기반으로 상태 업데이트 요청
                updateSaleStatus(holder.itemView.getContext(), userId, item.transactionId, "판매완료", new ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        item.status = "판매완료";
                        item.isSelected = false;
                        notifyItemChanged(holder.getAdapterPosition());
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(holder.itemView.getContext(), "상태 변경 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        holder.ivArrow.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    static class SalesViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTitle, tvPrice;
        ImageView ivBook, ivArrow;
        Button btnStatus;
        CheckBox checkbox;

        public SalesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ivBook = itemView.findViewById(R.id.iv_book);
            btnStatus = itemView.findViewById(R.id.btn_status);
            checkbox = itemView.findViewById(R.id.checkbox_select);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
        }
    }
}