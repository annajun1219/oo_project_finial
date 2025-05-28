package com.example.oo_frontend.UI.main.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oo_frontend.Model.CategoryItem;
import com.example.oo_frontend.R;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryItem> categoryList;

    public CategoryAdapter(Context context, List<CategoryItem> list) {
        this.context = context;
        this.categoryList = list;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryItem item = categoryList.get(position);

        // ✅ 전공 vs 기타 구분해서 다른 레이아웃 사용
        int layoutRes = item.getType().equals("etc")
                ? R.layout.item_category_etc
                : R.layout.item_category;

        View gridItem = LayoutInflater.from(context).inflate(layoutRes, parent, false);

        ImageView icon = gridItem.findViewById(R.id.categoryIcon);
        TextView label = gridItem.findViewById(R.id.categoryText);

        icon.setImageResource(item.getImageResId());
        label.setText(item.getName());

        return gridItem;
    }
}

