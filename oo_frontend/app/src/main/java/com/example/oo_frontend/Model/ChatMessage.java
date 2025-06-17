package com.example.oo_frontend.Model;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    @SerializedName("messageId")  // ✅ 백엔드에서 보내는 메시지 ID
    private Long messageId;

    @SerializedName("senderId")
    private Long senderId;

    @SerializedName("message")
    private String message;

    @SerializedName("sentAt")
    private String sentAt;

    public Long getMessageId() {
        return messageId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public String getSentAt() {
        return sentAt;
    }
}