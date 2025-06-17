package com.example.oo_frontend.UI.mypage.favorite;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.FavoriteItem;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context context;
    private List<FavoriteItem> list;

    public FavoriteAdapter(Context context, List<FavoriteItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = list.get(position);

        Glide.with(holder.itemView.getContext())
                .load(item.imageUrl)
                .placeholder(R.drawable.sample_book)
                .into(holder.imageBook);

        holder.tvTitle.setText(item.title);  // 책 제목
        holder.tvPrice.setText(String.valueOf(item.price));  // 책 가격

        if ("예약중".equals(item.status)) {
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.viewDim.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setVisibility(View.GONE);
            holder.viewDim.setVisibility(View.GONE);
        }

        // 하트 클릭 시 서버에 DELETE 요청
        holder.imageHeart.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            FavoriteItem selectedItem = list.get(pos);
            SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            long userId = prefs.getLong("userId", -1);  // ✅ long 으로 가져와야 함
            long bookId = selectedItem.bookId;

            RetrofitHelper.deleteFavorite(context, userId, bookId, new ApiCallback<String>() {
                @Override
                public void onSuccess(String msg) {
                    list.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, list.size());
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String msg) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBook, imageHeart;
        TextView tvStatus;
        TextView tvTitle, tvPrice;

        View viewDim;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R.id.image_book);
            imageHeart = itemView.findViewById(R.id.image_heart);
            tvStatus = itemView.findViewById(R.id.tv_status);
            viewDim = itemView.findViewById(R.id.view_dim);

            // ✨ 새로 추가
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

    }
}