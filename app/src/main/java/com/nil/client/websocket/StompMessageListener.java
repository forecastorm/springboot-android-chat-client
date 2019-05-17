package com.nil.client.websocket;

public interface StompMessageListener {

    void onMessage(StompMessage message);

}