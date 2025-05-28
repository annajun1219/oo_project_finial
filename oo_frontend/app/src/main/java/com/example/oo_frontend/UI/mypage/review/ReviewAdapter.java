package com.example.oo_frontend.UI.mypage.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.recyclerview.widget.RecyclerView;

import com.example.oo_frontend.Model.ReviewItem;
import com.example.oo_frontend.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewItem> reviewList;

    public ReviewAdapter(List<ReviewItem> reviewList) {
        this.reviewList = reviewList;
    }

    // ✅ ViewHolder: 각 리뷰 항목의 뷰 참조 바인딩
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textRating, textNickname, textCreatedAt, textContent, textProductName;
        LinearLayout keywordContainer;

        public ViewHolder(View view) {
            super(view);
            textRating = view.findViewById(R.id.textRating);
            textNickname = view.findViewById(R.id.textNickname);
            textCreatedAt = view.findViewById(R.id.textCreatedAt);
            textContent = view.findViewById(R.id.textContent);
            textProductName = view.findViewById(R.id.textProductName);
            keywordContainer = view.findViewById(R.id.keywordContainer);
        }

        // ✅ 키워드 태그 동적 생성
        public void bindKeywords(String[] keywords) {
            keywordContainer.removeAllViews();
            if (keywords == null) return;

            for (String keyword : keywords) {
                TextView tagView = new TextView(itemView.getContext());
                tagView.setText(keyword);
                tagView.setTextSize(12);
                tagView.setPadding(16, 8, 16, 8);
                tagView.setBackgroundResource(R.drawable.sales_card_border);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 16, 0);
                tagView.setLayoutParams(params);

                keywordContainer.addView(tagView);
            }
        }
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewItem item = reviewList.get(position);

        // ✅ 각 항목 바인딩
        holder.textRating.setText(String.valueOf(item.rating));
        holder.textNickname.setText(item.reviewerNickname);
        holder.textCreatedAt.setText(item.createdAt);
        holder.textContent.setText(item.content);
        holder.textProductName.setText(item.productName);
        holder.bindKeywords(item.keywords);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
