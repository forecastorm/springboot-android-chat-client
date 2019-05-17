package com.nil.client.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nil.client.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {


    public static final int currentUserType = 0;
    public static final int targetUserType = 1;

    private TextView chatItemMessageTextView;
    private ImageView chatItemProfileImageView;
    private int chatItemProfileImageViewType;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        chatItemMessageTextView = itemView.findViewById(R.id.chatItemMessageTextView);
        chatItemProfileImageView = itemView.findViewById(R.id.chatItemProfileImageView);
    }

    public TextView getChatItemMessageTextView() {
        return chatItemMessageTextView;
    }


    public ImageView getChatItemProfileImageView() {
        return chatItemProfileImageView;
    }

    public int getChatItemProfileImageViewType() {
        return chatItemProfileImageViewType;
    }

    public void setChatItemProfileImageViewType(int chatItemProfileImageViewType) {
        this.chatItemProfileImageViewType = chatItemProfileImageViewType;
    }
}
