package com.example.oo_frontend.UI.chat.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;

import com.example.oo_frontend.R;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout messageContainer;
    private EditText messageInput;
    private ScrollView messageScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
        Intent intent = getIntent();
        String sellerName = intent.getStringExtra("sellerName");
        String bookTitle = intent.getStringExtra("bookTitle");
        String bookPrice = intent.getStringExtra("bookPrice");
        int imageResId = intent.getIntExtra("bookImageResId", R.drawable.ic_book_placeholder); // 기본 이미지 fallback

        // ✅ 데이터 바인딩
        chatRoomTitle.setText(sellerName + "님과의 채팅");
        bookTitleView.setText(bookTitle);
        bookPriceView.setText(bookPrice + "원");
        bookImage.setImageResource(imageResId);

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
