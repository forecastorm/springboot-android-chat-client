package com.nil.client.websocket.socketclient;

import com.nil.client.websocket.CloseHandler;

import okhttp3.Response;
import okhttp3.WebSocket;

public class ChatListener extends SpringBootWebSocketClient {

    String senderID;
    String receiverID;

    public ChatListener(String senderID, String receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        sendConnectMessage(webSocket);
        for (String topic : topics.keySet()) {
            sendSubscribeMessage(webSocket, topic);
        }
        //turn the User with givenID's status On
        System.out.println("start listening Chat ");
        sendMessage(webSocket, "/app/chat/" + senderID + "/" + receiverID + "/" + "listen", "");
        closeHandler = new CloseHandler(webSocket);
    }
}
