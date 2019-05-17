package com.nil.client.websocket.socketclient;

import com.nil.client.websocket.CloseHandler;
import com.nil.client.websocket.StompMessage;
import com.nil.client.websocket.StompMessageSerializer;
import com.nil.client.websocket.TopicHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class SpringBootWebSocketClient extends WebSocketListener {

    public Map<String, TopicHandler> topics = new HashMap<>();
    public CloseHandler closeHandler;
    public String id = "sub-001";
    public WebSocket webSocket;

    public SpringBootWebSocketClient() {

    }

    public SpringBootWebSocketClient(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public TopicHandler subscribe(String topic) {
        TopicHandler handler = new TopicHandler(topic);
        topics.put(topic, handler);
        return handler;
    }


    public void unSubscribe(String topic) {
        topics.remove(topic);
    }

    public TopicHandler getTopicHandler(String topic) {
        if (topics.containsKey(topic)) {
            return topics.get(topic);
        }
        return null;
    }


    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void connect(String address) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newWebSocket(request, this);

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {

        this.webSocket = webSocket;
        sendConnectMessage(webSocket);

        for (String topic : topics.keySet()) {
            sendSubscribeMessage(webSocket, topic);
        }
        closeHandler = new CloseHandler(webSocket);
    }


    public void sendConnectMessage(WebSocket webSocket) {

        StompMessage message = new StompMessage("CONNECT");
        message.put("accept-version", "1.1");
        message.put("heart-beat", "10000,10000");
        webSocket.send(StompMessageSerializer.serialize(message));
    }

    public void sendSubscribeMessage(WebSocket webSocket, String topic) {

        StompMessage message = new StompMessage("SUBSCRIBE");

        message.put("id", id);
        message.put("destination", topic);

        webSocket.send(StompMessageSerializer.serialize(message));
    }

    public void disconnect() {
        if (webSocket != null) {
            closeHandler.close();
            webSocket = null;
            closeHandler = null;
        }
    }

    public boolean isConnected() {
        return closeHandler != null;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {

        StompMessage message = StompMessageSerializer.deserialize(text);
        String topic = message.getHeader("destination");
        if (topics.containsKey(topic)) {
            topics.get(topic).onMessage(message);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        //System.out.println("MESSAGE: " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        System.out.println("CLOSE: " + code + " " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    public void sendMessage(WebSocket webSocket, String topic, String content) {
        StompMessage message = new StompMessage("SEND");
        message.put("destination", topic);
        message.setContent(content);
        String serializedMessage = StompMessageSerializer.serialize(message);
        webSocket.send(serializedMessage);
    }
}
