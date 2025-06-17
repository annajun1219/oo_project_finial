package com.example.oo_frontend.Model;

import com.google.gson.annotations.SerializedName;

public class ChatRoom {

    @SerializedName("roomId")
    private Long roomId;

    @SerializedName("bookId")
    private Long bookId;


    @SerializedName("otherUserId")
    private String otherUserId;

    @SerializedName("otherUserName")
    private String otherUserName;

    @SerializedName("otherUserProfileImage")
    private String otherUserProfileImage;

    @SerializedName("lastMessage")
    private String lastMessage;

    @SerializedName("lastSentAt")
    private String lastSentAt;

    // Getter
    public Long getRoomId() {
        return roomId;
    }

    public Long getBookId() {
        return bookId;
    }


    public String getOtherUserId() {
        return otherUserId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public String getOtherUserProfileImage() {
        return otherUserProfileImage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastSentAt() {
        return lastSentAt;
    }
}

