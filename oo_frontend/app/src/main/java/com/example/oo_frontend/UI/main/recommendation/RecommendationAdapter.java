package com.example.oo_frontend.UI.main.recommendation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Model.Recommendation;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.main.book.BookDetailActivity;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    private List<Book> books;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectView, professorView;
        Button nextBtn;

        public ViewHolder(View view) {
            super(view);
            subjectView = view.findViewById(R.id.textSubject);
            professorView = view.findViewById(R.id.textProfessor);
            nextBtn = view.findViewById(R.id.btnNext);
        }
    }

    public RecommendationAdapter(List<Book> data) {
        this.books = data;
    }

    @Override
    public RecommendationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommend_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book item = books.get(position);
        holder.subjectView.setText(item.getTitle());
        holder.professorView.setText(item.getProfessorName());

        holder.nextBtn.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("productId", item.getProductId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}