package com.example.oo_frontend.UI.mypage.sales;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.SaleItem;
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

        Glide.with(holder.itemView.getContext())
                .load(item.imageUrl)
                .placeholder(R.drawable.sample_book)
                .into(holder.ivBook);

        holder.checkbox.setVisibility(showCheckboxes ? View.VISIBLE : View.GONE);
        holder.checkbox.setChecked(item.isSelected);
        holder.checkbox.setOnCheckedChangeListener((btnView, isChecked) -> item.isSelected = isChecked);

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