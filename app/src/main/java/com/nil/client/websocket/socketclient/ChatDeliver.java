package com.nil.client.websocket.socketclient;

import com.nil.client.websocket.CloseHandler;

import okhttp3.Response;
import okhttp3.WebSocket;

public class ChatDeliver extends SpringBootWebSocketClient {

    private String senderID;
    private String receiverID;
    private String content;


    public ChatDeliver(String senderID, String receiverID, String content) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        sendConnectMessage(webSocket);
        for (String topic : topics.keySet()) {
            sendSubscribeMessage(webSocket, topic);
        }

        String destination = "/app/chat/" + senderID + "/" + receiverID;
        sendMessage(webSocket, destination, content);
        closeHandler = new CloseHandler(webSocket);
    }


}
