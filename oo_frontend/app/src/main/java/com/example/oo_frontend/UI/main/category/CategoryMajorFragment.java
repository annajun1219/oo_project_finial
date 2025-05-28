package com.example.oo_frontend.UI.main.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.oo_frontend.Model.CategoryItem;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryMajorFragment extends Fragment {

    public CategoryMajorFragment() {
        // 기본 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_major, container, false);
        GridView gridView = view.findViewById(R.id.categoryMajorGrid);

        List<CategoryItem> categoryList = new ArrayList<>();
        categoryList.add(new CategoryItem("문과대학", R.drawable.ic_liberal_1,"major"));
        categoryList.add(new CategoryItem("이과대학", R.drawable.ic_science_2,"major"));
        categoryList.add(new CategoryItem("공과대학", R.drawable.ic_engineering_3,"major"));
        categoryList.add(new CategoryItem("생활과학", R.drawable.ic_life_4,"major"));
        categoryList.add(new CategoryItem("사회과학", R.drawable.ic_social_5,"major"));
        categoryList.add(new CategoryItem("법과대학", R.drawable.ic_law_6,"major"));
        categoryList.add(new CategoryItem("경상대학", R.drawable.ic_economy_7,"major"));
        categoryList.add(new CategoryItem("음악대학", R.drawable.ic_music_8,"major"));
        categoryList.add(new CategoryItem("약학대학", R.drawable.ic_pharmacy_9,"major"));
        categoryList.add(new CategoryItem("미술대학", R.drawable.ic_art_10,"major"));

        com.example.oo_frontend.UI.main.category.CategoryAdapter adapter = new com.example.oo_frontend.UI.main.category.CategoryAdapter(getContext(), categoryList);
        gridView.setAdapter(adapter);

        // ✅ 클릭 시 MainActivity의 onCategoryClicked() 호출
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            CategoryItem selected = categoryList.get(position);
            ((MainActivity) requireActivity()).onCategoryClicked(selected.getName());
        });

        return view;
    }


}

