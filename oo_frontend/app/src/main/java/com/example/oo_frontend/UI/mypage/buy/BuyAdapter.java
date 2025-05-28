package com.example.oo_frontend.UI.mypage.buy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.PurchaseItem;
import com.example.oo_frontend.R;

import java.util.List;

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder> {

    private List<PurchaseItem> buyList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView title, price;
        Button btnReview, btnStatus;

        public ViewHolder(View view) {
            super(view);
            bookImage = view.findViewById(R.id.iv_book);
            title = view.findViewById(R.id.tv_title);
            price = view.findViewById(R.id.tv_price);
            btnReview = view.findViewById(R.id.btn_review);
            btnStatus = view.findViewById(R.id.btn_status);
        }
    }

    public BuyAdapter(Context context, List<PurchaseItem> buyList) {
        this.context = context;
        this.buyList = buyList;
    }

    @Override
    public BuyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_buy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PurchaseItem item = buyList.get(position);
        holder.title.setText(item.title);
        holder.price.setText(item.price + "원");
        Glide.with(context).load(item.imageUrl).into(holder.bookImage);

        if ("COMPLETED".equals(item.status)) {
            holder.btnStatus.setText("구매완료");
        } else if ("IN_PROGRESS".equals(item.status)) {
            holder.btnStatus.setText("예약중");
        }

        // 후기 버튼: 작성된 경우만 표시
        if (item.hasReview) {
            holder.btnReview.setVisibility(View.VISIBLE);
        } else {
            holder.btnReview.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return buyList.size();
    }

    public void updateList(List<PurchaseItem> filteredList) {
        this.buyList = filteredList;
        notifyDataSetChanged();
    }
}
