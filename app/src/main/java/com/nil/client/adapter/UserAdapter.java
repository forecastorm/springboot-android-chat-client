package com.nil.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nil.client.MessageActivity;
import com.nil.client.R;
import com.nil.client.model.User;
import com.nil.client.viewholder.UserViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context context;
    private List<User> userList;
    private User currentUser;

    public UserAdapter(Context context, List<User> userList, User currentUser) {
        this.context = context;
        this.userList = userList;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_item, viewGroup, false);
        // returning user view holder has all fields assigned
        return new UserViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, final int i) {

        //remove the current user from the userList
        User user = userList.get(i);


        userViewHolder.getUserNameTextView().setText(user.getUsername());
        //set profile image for user
        if (user.getImageURL().equals("null")) {
            if (user.getGender().equals("Male")) {
                userViewHolder.getUserProfileImageView().setImageResource(R.drawable.profile_default_male);
            }
            if (user.getGender().equals("Female")) {
                userViewHolder.getUserProfileImageView().setImageResource(R.drawable.profile_default_female);
            }
        }
        //set profile image for user ends

        //set status image for user
        if (user.getStatus().equals("-1")) {
            userViewHolder.getStatusOnImageView().setVisibility(View.GONE);
            userViewHolder.getStatusOffImageView().setVisibility(View.VISIBLE);
        }

        if (user.getStatus().equals("0")) {
            userViewHolder.getStatusOnImageView().setVisibility(View.VISIBLE);
            userViewHolder.getStatusOffImageView().setVisibility(View.GONE);
        }
        //set status image for user ends



        //set targetUser in user view holder

        userViewHolder.setTargetUser(user);

        //set targetUser in user view holder ends


        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("CurrentUserKey", currentUser);
                intent.putExtra("TargetUserKey", userViewHolder.getTargetUser());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
