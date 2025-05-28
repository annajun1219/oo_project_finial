package com.example.oo_frontend.UI.main.recommendation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oo_frontend.Model.Recommendation;
import com.example.oo_frontend.R;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    private List<Recommendation> recommendations;
    private ViewPager2 viewPager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectView, professorView;
        ImageButton nextBtn;

        public ViewHolder(View view) {
            super(view);
            subjectView = view.findViewById(R.id.textSubject);
            professorView = view.findViewById(R.id.textProfessor);
            nextBtn = view.findViewById(R.id.btnNext);
        }
    }

    public RecommendationAdapter(List<Recommendation> data, ViewPager2 viewPager) {
        this.recommendations = data;
        this.viewPager = viewPager;
    }

    @Override
    public RecommendationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommend_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recommendation item = recommendations.get(position);
        holder.subjectView.setText("과목: " + item.getSubjectName());
        holder.professorView.setText("교수: " + item.getProfessor());
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }
}
