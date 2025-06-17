package com.example.oo_frontend.UI.chat.room;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Model.ChatMessage;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.chat.room.ChatMessageAdapter;
import com.example.oo_frontend.UI.chat.list.ChatListActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private ListView messageListView;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> messageList;
    private EditText messageEditText;
    private ImageView sendButton;
    private TextView confirmTransactionButton;
    private Long roomId; // ✅ String → Long
    private Long userId; // ✅ int → Long
    private Long bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // ✅ 채팅방 이름 및 사용자 정보
        roomId = getIntent().getLongExtra("roomId", -1L);
        bookId = getIntent().getLongExtra("bookId", -1L);


        String userName = getIntent().getStringExtra("userName");
        userId = getIntent().getLongExtra("userId", -1L);

        if (roomId == -1L || userId == -1L || bookId == -1L) {
            Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RetrofitHelper.fetchBookDetail(this, bookId, userId, new ApiCallback<Book>() {
            @Override
            public void onSuccess(Book book) {
                TextView bookTitle = findViewById(R.id.bookTitle);
                TextView bookPrice = findViewById(R.id.bookPrice);
                ImageView bookImage = findViewById(R.id.bookImage);

                bookTitle.setText(book.getTitle());
                bookPrice.setText(book.getPrice() + "원");

                Glide.with(ChatRoomActivity.this)
                        .load(book.getImageUrl())
                        .placeholder(R.drawable.ic_book_placeholder)
                        .error(R.drawable.ic_book_placeholder)
                        .into(bookImage);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ChatRoomActivity.this, "책 정보 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });


        TextView titleView = findViewById(R.id.chatRoomTitle);
        titleView.setText(userName);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatListActivity.class));
            finish();
        });

        // ✅ 메시지 리스트 초기화
        messageListView = findViewById(R.id.messageListView);
        messageList = new ArrayList<>();
        adapter = new ChatMessageAdapter(this, messageList, userId.intValue()); // 여긴 int로 유지
        messageListView.setAdapter(adapter);

        // ✅ 메시지 입력 및 전송
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            String text = messageEditText.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                messageEditText.setText("");
            }
        });

        // ✅ 거래확정 버튼
        confirmTransactionButton = findViewById(R.id.confirmTransactionButton);
        confirmTransactionButton.setVisibility(View.VISIBLE);
        confirmTransactionButton.setOnClickListener(v -> {
            Toast.makeText(this, "거래가 확정되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // ✅ 서버에서 메시지 불러오기
        loadMessages();
    }

    private void loadMessages() {
        RetrofitHelper.fetchChatMessages(this, roomId, userId, new ApiCallback<List<ChatMessage>>() {
            @Override
            public void onSuccess(List<ChatMessage> data) {
                messageList.clear();
                messageList.addAll(data);
                adapter.notifyDataSetChanged();
                messageListView.post(() -> messageListView.setSelection(adapter.getCount() - 1));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ChatRoomActivity.this, "메시지 불러오기 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String messageText) {
        RetrofitHelper.sendChatMessage(this, userId, roomId, messageText, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loadMessages(); // 전송 성공 후 새로고침
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ChatRoomActivity.this, "메시지 전송 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
