package com.nil.client.websocket.socketclient;

import com.nil.client.websocket.CloseHandler;

import okhttp3.Response;
import okhttp3.WebSocket;

public class StatusOffWebSocketClient extends SpringBootWebSocketClient {
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        sendConnectMessage(webSocket);
        for (String topic : topics.keySet()) {
            sendSubscribeMessage(webSocket, topic);
        }
        // turn the givenId user status off
        sendMessage(webSocket, "/app/userList/status/off", "1");
        closeHandler = new CloseHandler(webSocket);
    }
}