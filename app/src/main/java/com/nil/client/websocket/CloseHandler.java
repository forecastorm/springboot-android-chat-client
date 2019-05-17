package com.nil.client.websocket;

import okhttp3.WebSocket;

public class CloseHandler {

    private final WebSocket webSocket;

    public CloseHandler(WebSocket webSocket){
        this.webSocket = webSocket;
    }

    public void close(){
        webSocket.close(1000,"close websocket");
    }

}
