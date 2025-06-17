package com.example.oo_frontend.UI.chat.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oo_frontend.Model.ChatRoom;
import com.example.oo_frontend.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatListAdapter extends BaseAdapter {

    private Context context;
    private List<ChatRoom> chatRoomList;

    public ChatListAdapter(Context context, List<ChatRoom> chatRoomList) {
        this.context = context;
        this.chatRoomList = chatRoomList;
    }

    @Override
    public int getCount() {
        return chatRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 뷰홀더 패턴
    static class ViewHolder {
        ImageView imageProfile;
        TextView textUserName;
        TextView textLastMessage;
        TextView textLastSentAt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatRoom item = chatRoomList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false);
            holder = new ViewHolder();
            holder.imageProfile = convertView.findViewById(R.id.imageProfile);
            holder.textUserName = convertView.findViewById(R.id.textUserName);
            holder.textLastMessage = convertView.findViewById(R.id.textLastMessage);
            holder.textLastSentAt = convertView.findViewById(R.id.textLastSentAt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 데이터 바인딩
        holder.textUserName.setText(item.getOtherUserName());
        holder.textLastMessage.setText(item.getLastMessage());
        holder.textLastSentAt.setText(formatDate(item.getLastSentAt()));
        holder.imageProfile.setImageResource(R.drawable.ic_profile_placeholder);
        return convertView;
    }

    // 날짜 포맷 변환: "2025-04-08T10:30:00" → "4월 8일 10:30"
    private String formatDate(String isoDate) {
        if (isoDate == null || isoDate.trim().isEmpty()) {  // ✅ 변수명 일치시킴
            return "";
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA);
            Date date = inputFormat.parse(isoDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("M월 d일 HH:mm", Locale.KOREA);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // 에러 시 빈 문자열 반환
        }
    }
}