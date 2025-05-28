package com.example.oo_frontend.UI.chat.list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oo_frontend.Model.ChatRoom;
import com.example.oo_frontend.Network.ApiCallback;
import com.example.oo_frontend.Network.RetrofitHelper;
import com.example.oo_frontend.R;
import com.example.oo_frontend.UI.chat.room.ChatRoomActivity;
import com.example.oo_frontend.UI.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private ListView chatListView;
    private List<ChatRoom> chatRooms;
    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatListView = findViewById(R.id.chatListView);

        // ✅ JWT 없이 채팅방 리스트 불러오기
        RetrofitHelper.fetchChatRooms(this, new ApiCallback<List<ChatRoom>>() {
            @Override
            public void onSuccess(List<ChatRoom> result) {
                chatRooms = result;
                adapter = new ChatListAdapter(ChatListActivity.this, chatRooms);
                chatListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ChatListActivity.this, "채팅 목록 불러오기 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ 채팅방 클릭 시 이동
        chatListView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            ChatRoom selectedChat = chatRooms.get(position);
            Intent intent = new Intent(ChatListActivity.this, ChatRoomActivity.class);
            intent.putExtra("userName", selectedChat.getOtherUserName());
            intent.putExtra("roomId", selectedChat.getRoomId());
            startActivity(intent);
        });

        // ✅ 하단 네비게이션 바
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_chat);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(ChatListActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}
