package com.example.oo_frontend.UI.main.book;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.R;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> originalList;
    private List<Book> filteredList;
    private Context context;

    public BookAdapter(List<Book> bookList, Context context) {
        this.originalList = new ArrayList<>(bookList);
        this.filteredList = new ArrayList<>(bookList);
        this.context = context;
    }

    public void updateBooks(List<Book> newList) {
        originalList.clear();
        originalList.addAll(newList);

        filteredList.clear();
        filteredList.addAll(newList);

        notifyDataSetChanged();
    }
    // 필터 함수
    public void filter(String keyword, boolean byTitle) {
        filteredList.clear();

        if (keyword.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (Book book : originalList) {
                if (byTitle && book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(book);
                } else if (!byTitle && book.getProfessorName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(book);
                }
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = filteredList.get(position);

        holder.title.setText(book.getTitle());
        holder.salePrice.setText(book.getPrice() + "원");
        holder.originalPrice.setText("정가 " + book.getOfficialPrice() + "원");

        String meta = "시세 " + book.getAverageUsedPrice() + "원 · " + book.getProfessorName() + " · " + book.getCategory();
        String professor = book.getProfessorName() != null ? book.getProfessorName() : "교수명 없음";
        String category = book.getCategory() != null ? book.getCategory() : "카테고리 없음";
        String meta_search = "교수: " + professor + " · " + category;

        holder.metaInfo.setText(meta_search);

        String imageUrl = book.getImageUrl();


        // 이미지 로딩
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.book_sample1)
                .error(R.drawable.book_sample1)
                .into(holder.image);

        // 🔁 클릭 시 상세페이지로 이동
        holder.itemView.setOnClickListener(v -> {
            Long productId = book.getProductId();
            if (productId != null) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("productId", productId);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "교재 ID가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, salePrice, originalPrice, metaInfo;
        ImageView image;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitle);
            salePrice = itemView.findViewById(R.id.bookSalePrice);
            originalPrice = itemView.findViewById(R.id.bookOriginalPrice);
            metaInfo = itemView.findViewById(R.id.bookMetaInfo);
            image = itemView.findViewById(R.id.bookImage);
        }
    }
}
