package com.example.oo_frontend.UI.chat.room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;

import com.bumptech.glide.Glide;
import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Model.ChatMessage;
import com.example.oo_frontend.Network.RetrofitClient;
import com.example.oo_frontend.Network.RetrofitService;
import com.example.oo_frontend.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout messageContainer;
    private EditText messageInput;
    private ScrollView messageScroll;

    private Long userId;
    private Long roomId;
    private RetrofitService retrofitService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // ✅ 올바른 방식: 클래스 필드에 할당
        this.userId = getIntent().getLongExtra("userId", -1);
        this.roomId = getIntent().getLongExtra("roomId", -1);
        Long bookId = getIntent().getLongExtra("bookId", -1);  // 이건 지역 변수 그대로 OK

        Log.d("ChatDebug", "📦 전달받은 bookId = " + bookId);


        // 🔗 View 연결
        TextView chatRoomTitle = findViewById(R.id.chatRoomTitle);
        TextView bookTitleView = findViewById(R.id.bookTitle);
        TextView bookPriceView = findViewById(R.id.bookPrice);
        ImageView bookImage = findViewById(R.id.bookImage);
        ImageView backButton = findViewById(R.id.backButton);
        Button sendButton = findViewById(R.id.sendButton);
        messageContainer = findViewById(R.id.messageContainer);
        messageInput = findViewById(R.id.messageInput);
        messageScroll = findViewById(R.id.messageScroll);

        // ✅ Intent 데이터 받기

        /*
        Intent intent = getIntent();
        String sellerName = intent.getStringExtra("sellerName");
        String bookTitle = intent.getStringExtra("bookTitle");
        String bookPrice = intent.getStringExtra("bookPrice");

        String imageUrl = getIntent().getStringExtra("bookImageUrl");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_book_placeholder)
                    .into(bookImage);
        } else {
            bookImage.setImageResource(R.drawable.ic_book_placeholder);
        }

        // ✅ 데이터 바인딩
        chatRoomTitle.setText(sellerName + "님과의 채팅");
        bookTitleView.setText(bookTitle);
        bookPriceView.setText(bookPrice + "원");

        */


        // ✅ Retrofit 초기화
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);


// ✅ 책 정보 불러오기 (bookId가 유효할 때만)
        if (bookId != -1) {
            retrofitService.getBookDetail(bookId, userId).enqueue(new Callback<Book>() {
                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Book book = response.body();

                        bookTitleView.setText(book.getTitle());
                        bookPriceView.setText(book.getPrice() + "원");

                        Glide.with(ChatActivity.this)
                                .load(book.getImageUrl())
                                .placeholder(R.drawable.ic_book_placeholder)
                                .error(R.drawable.ic_book_placeholder)
                                .into(bookImage);

                        // 채팅방 타이틀도 seller 정보로 설정
                        chatRoomTitle.setText(book.getSeller().getName() + "님과의 채팅");
                    }
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {
                    Log.e("ChatActivity", "책 정보 불러오기 실패", t);
                }
            });
        }

        // ✅ 메시지 목록 가져오기
        loadMessages();

        // ✅ 메시지 전송 처리
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                addMessageToChat(message, true);  // 바로 UI에 반영
                messageInput.setText("");
                messageScroll.post(() -> messageScroll.fullScroll(View.FOCUS_DOWN));
            }
        });

        // ✅ 뒤로가기 버튼 처리
        backButton.setOnClickListener(v -> finish());

        // ✅ 채팅 전송 버튼 처리
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                addMessageToChat(message, true);  // 사용자 메시지 추가
                messageInput.setText("");
                messageScroll.post(() -> messageScroll.fullScroll(View.FOCUS_DOWN));
            }
        });
    }

    // ✅ 메시지 불러오기
    private void loadMessages() {
        retrofitService.getChatMessages(roomId, userId).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ChatMessage msg : response.body()) {
                        boolean isUser = msg.getSenderId().equals(userId);
                        addMessageToChat(msg.getMessage(), isUser);
                    }
                    messageScroll.post(() -> messageScroll.fullScroll(View.FOCUS_DOWN));
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                Log.e("ChatActivity", "메시지 불러오기 실패", t);
            }
        });
    }

    // ✅ 메시지 전송
    private void sendMessage(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);

        retrofitService.sendMessageToChatRoom(userId, roomId, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // 성공해도 이미 UI에 추가했으니 따로 처리 X
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ChatActivity", "메시지 전송 실패", t);
            }
        });
    }


    // ✅ 채팅 메시지를 레이아웃에 추가하는 메서드
    private void addMessageToChat(String message, boolean isUser) {
        TextView messageView = new TextView(this);
        messageView.setText(message);
        messageView.setTextSize(16);
        messageView.setTextColor(getResources().getColor(android.R.color.black));
        messageView.setPadding(16, 8, 16, 8);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 8);


        if (isUser) {
            params.gravity = Gravity.END; // 오른쪽 정렬
            messageView.setBackgroundResource(R.drawable.bg_message_right);
        } else {
            params.gravity = Gravity.START; // 왼쪽 정렬
            messageView.setBackgroundResource(R.drawable.bg_message_left);
        }

        messageView.setLayoutParams(params);
        messageContainer.addView(messageView);
    }
}