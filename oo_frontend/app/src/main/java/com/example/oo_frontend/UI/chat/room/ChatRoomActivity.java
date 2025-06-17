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
    private Long roomId;
    private Long userId;
    private Long bookId;
    private Long sellerId;  // ✅ 필요 시 Glide 후 sellerId도 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        roomId = getIntent().getLongExtra("roomId", -1L);
        bookId = getIntent().getLongExtra("bookId", -1L);
        userId = getIntent().getLongExtra("userId", -1L);
        String userName = getIntent().getStringExtra("userName");

        if (roomId == -1L || userId == -1L || bookId == -1L) {
            Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView bookTitle = findViewById(R.id.bookTitle);
        TextView bookPrice = findViewById(R.id.bookPrice);
        ImageView bookImage = findViewById(R.id.bookImage);

        RetrofitHelper.fetchBookDetail(this, bookId, userId, new ApiCallback<Book>() {
            @Override
            public void onSuccess(Book book) {
                bookTitle.setText(book.getTitle());
                bookPrice.setText(book.getPrice() + "원");
                sellerId = book.getSellerId();  // ✅ sellerId 저장

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

        messageListView = findViewById(R.id.messageListView);
        messageList = new ArrayList<>();
        adapter = new ChatMessageAdapter(this, messageList, userId.intValue());
        messageListView.setAdapter(adapter);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            String text = messageEditText.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                messageEditText.setText("");
            }
        });

        confirmTransactionButton = findViewById(R.id.confirmTransactionButton);
        confirmTransactionButton.setVisibility(View.VISIBLE);
        confirmTransactionButton.setOnClickListener(v -> {
            if (sellerId == null) {
                Toast.makeText(this, "판매자 정보를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ 1단계: 거래 생성
            RetrofitHelper.createTransaction(this, bookId, sellerId, userId, new ApiCallback<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(ChatRoomActivity.this, "거래 생성 완료!", Toast.LENGTH_SHORT).show();

                    // ✅ 2단계: 거래 ID 조회
                    RetrofitHelper.getTransactionId(ChatRoomActivity.this, bookId, sellerId, userId, new ApiCallback<Long>() {
                        @Override
                        public void onSuccess(Long transactionId) {

                            // ✅ 3단계: 상태 업데이트
                            RetrofitHelper.updateSaleStatus(ChatRoomActivity.this, sellerId, transactionId, "예약중", new ApiCallback<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ChatRoomActivity.this, "예약중으로 상태 변경 완료", Toast.LENGTH_SHORT).show();
                                    confirmTransactionButton.setEnabled(false);
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Toast.makeText(ChatRoomActivity.this, "상태 변경 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(ChatRoomActivity.this, "거래 ID 조회 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ChatRoomActivity.this, "거래 생성 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

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
                loadMessages();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ChatRoomActivity.this, "메시지 전송 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
