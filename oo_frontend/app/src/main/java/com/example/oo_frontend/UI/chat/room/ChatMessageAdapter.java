package com.example.oo_frontend.UI.chat.room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oo_frontend.Model.ChatMessage;
import com.example.oo_frontend.R;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> messageList;
    private int myUserId;  // ✅ 현재 사용자 ID

    public ChatMessageAdapter(Context context, List<ChatMessage> messageList, int myUserId) {
        this.context = context;
        this.messageList = messageList;
        this.myUserId = myUserId;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_message_item, parent, false);
        }

        ChatMessage message = messageList.get(position);

        LinearLayout leftLayout = view.findViewById(R.id.leftMessageLayout);
        LinearLayout rightLayout = view.findViewById(R.id.rightMessageLayout);
        TextView leftText = view.findViewById(R.id.leftMessageText);
        TextView rightText = view.findViewById(R.id.rightMessageText);

        // ✅ 내 메시지인지 판단
        if (message.getSenderId() == myUserId) {
            // 오른쪽 말풍선 (내 메시지)
            leftLayout.setVisibility(View.GONE);
            rightLayout.setVisibility(View.VISIBLE);
            rightText.setText(message.getMessage());
        } else {
            // 왼쪽 말풍선 (상대방 메시지)
            rightLayout.setVisibility(View.GONE);
            leftLayout.setVisibility(View.VISIBLE);
            leftText.setText(message.getMessage());
        }

        return view;
    }
}
