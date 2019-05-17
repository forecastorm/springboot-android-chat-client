package com.nil.client.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nil.client.R;
import com.nil.client.adapter.UserAdapter;
import com.nil.client.config.InternetAddress;
import com.nil.client.model.User;
import com.nil.client.websocket.StompMessage;
import com.nil.client.websocket.StompMessageListener;
import com.nil.client.websocket.StompMessageSerializer;
import com.nil.client.websocket.TopicHandler;
import com.nil.client.websocket.socketclient.UserListListener;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private UserListListener userListListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //get extras

        assert getArguments() != null;
        final User currentUser = (User) getArguments().getSerializable("CurrentUserKey");
        //get extras ends

        userList = new ArrayList<>();
        userListListener = new UserListListener();
        TopicHandler topicHandler = userListListener.subscribe("/topics/userList");
        topicHandler.addListener(new StompMessageListener() {
            @Override
            public void onMessage(final StompMessage message) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userList.clear();
                        assert currentUser != null;
                        userList = StompMessageSerializer.putUserListStompMessageToListOfUsers(message, currentUser);
                        userAdapter = new UserAdapter(getContext(), userList, currentUser);
                        recyclerView.setAdapter(userAdapter);
                    }
                });
            }
        });
        userListListener.connect(InternetAddress.webSocketAddress);
        return view;
    }


}
