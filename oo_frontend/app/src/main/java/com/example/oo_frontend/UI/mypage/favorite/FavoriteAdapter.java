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

        // 이미지 로딩
        Glide.with(holder.itemView.getContext())
                .load(item.imageUrl)
                .placeholder(R.drawable.sample_book)
                .into(holder.imageBook);

        // 예약중일 경우 흐리게 표시
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
            int userId = prefs.getInt("userId", -1);
            if (userId == -1) return;

            RetrofitHelper.deleteFavorite(context, userId, selectedItem.bookId, new ApiCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    list.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, list.size());
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
        View viewDim;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R.id.image_book);
            imageHeart = itemView.findViewById(R.id.image_heart);
            tvStatus = itemView.findViewById(R.id.tv_status);
            viewDim = itemView.findViewById(R.id.view_dim);
        }
    }
}
