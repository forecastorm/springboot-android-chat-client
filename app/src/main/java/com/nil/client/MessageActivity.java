package com.nil.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nil.client.adapter.MessageAdapter;
import com.nil.client.config.InternetAddress;
import com.nil.client.model.Chat;
import com.nil.client.model.User;
import com.nil.client.websocket.StompMessage;
import com.nil.client.websocket.StompMessageListener;
import com.nil.client.websocket.StompMessageSerializer;
import com.nil.client.websocket.TopicHandler;
import com.nil.client.websocket.socketclient.ChatDeliver;
import com.nil.client.websocket.socketclient.ChatListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView targetUserNameTextView;
    EditText messageEditText;
    ImageButton messageSendButton;

    User currentUser;
    User targetUser;

    ChatDeliver chatDeliver;
    ChatListener chatListener;

    MessageAdapter messageAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        //get extras
        currentUser = (User) getIntent().getSerializableExtra("CurrentUserKey");
        targetUser = (User) getIntent().getSerializableExtra("TargetUserKey");
        //get extras ends


        //find view
        targetUserNameTextView = findViewById(R.id.targetUserNameTextView);
        messageEditText = findViewById(R.id.messageEditText);
        messageSendButton = findViewById(R.id.messageSendButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        //find view ends

        //set targetUserNameTextView
        targetUserNameTextView.setText(targetUser.getUsername());
        //set targetUserNameTextView ends


        //config toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //when back arrow button is clicked
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatListener.disconnect();
                finish();
            }
        });
        //config toolbar ends


        //config recycler view
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //config recycler view ends


        //start listening to chat list for current user and target user
        chatListener = new ChatListener(currentUser.getId(), targetUser.getId());
        TopicHandler topicHandler = chatListener.subscribe("/topics/event/" + currentUser.getId() + "/" + targetUser.getId());

        topicHandler.addListener(new StompMessageListener() {
            @Override
            public void onMessage(final StompMessage message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (message.getContent().equals("[]")) {
                            return;
                        }
                        StompMessageSerializer stompMessageSerializer = new StompMessageSerializer();
                        chats = new ArrayList<>();
                        chats = stompMessageSerializer.putChatListStompMessageToListOfChats(message);
                        messageAdapter = new MessageAdapter(MessageActivity.this, chats, currentUser, targetUser);
                        recyclerView.setAdapter(messageAdapter);
                    }
                });
            }
        });
        chatListener.connect(InternetAddress.webSocketAddress);

        //use deliver to invoke initial response
//        chatDeliver = new ChatDeliver(currentUser.getId(), targetUser.getId(), "");
//        chatDeliver.connect(InternetAddress.webSocketAddress);
//        chatDeliver.disconnect();

        //start listening to chat list for current user and target user ends


        //send message
        messageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageEditText.getText().toString().equals("")) {
                    Toast.makeText(MessageActivity.this, "You can not send empty message", Toast.LENGTH_SHORT).show();
                } else {
                    chatDeliver = new ChatDeliver(currentUser.getId(), targetUser.getId(), messageEditText.getText().toString());
                    chatDeliver.connect(InternetAddress.webSocketAddress);
                    chatDeliver.disconnect();
                }
                messageEditText.setText("");
            }
        });
        //send message ends
    }
}
