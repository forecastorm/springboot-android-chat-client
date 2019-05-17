package com.nil.client.websocket;

import com.nil.client.model.Chat;
import com.nil.client.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StompMessageSerializer {

    public static String serialize(StompMessage message) {

        StringBuilder buffer = new StringBuilder();

        buffer.append(message.getCommand() + "\n");

        for (Map.Entry<String, String> header : message.getHeaders().entrySet()) {
            buffer.append(header.getKey()).append(":").append(header.getValue()).append("\n");
        }

        buffer.append("\n");
        buffer.append(message.getContent());
        buffer.append('\0');

        //System.out.println(buffer.toString());

        return buffer.toString();
    }

    public static StompMessage deserialize(String message) {
        String[] lines = message.split("\n");

        String command = lines[0].trim();

        StompMessage result = new StompMessage(command);
        int i = 1;
        for (; i < lines.length; ++i) {
            String line = lines[i].trim();
            if (line.equals("")) {
                break;
            }
            String[] parts = line.split(":");
            String name = parts[0].trim();
            String value = "";
            if (parts.length == 2) {
                value = parts[1].trim();
            }
            result.put(name, value);
        }

        StringBuilder sb = new StringBuilder();

        for (; i < lines.length; ++i) {
            sb.append(lines[i]);
        }

        String body = sb.toString().trim();

        result.setContent(body);
        return result;
    }

    public static List<User> putUserListStompMessageToListOfUsers(StompMessage stompMessage, User currentUser) {

        String stompMessageString = stompMessage.getContent();

        List<User> userList = new ArrayList<>();
        while (true) {

            int idStart = stompMessageString.indexOf(':');
            int idEnd = stompMessageString.indexOf(',');
            String id = stompMessageString.substring(idStart + 1, idEnd);
            stompMessageString = stompMessageString.substring(idEnd + 1).replace("\"", "");


            int userNameStart = stompMessageString.indexOf(':');
            int userNameEnd = stompMessageString.indexOf(',');
            String userName = stompMessageString.substring(userNameStart + 1, userNameEnd).replace("\"", "");
            stompMessageString = stompMessageString.substring(userNameEnd + 1);

            int genderStart = stompMessageString.indexOf(':');
            int genderEnd = stompMessageString.indexOf(',');
            String gender = stompMessageString.substring(genderStart + 1, genderEnd).replace("\"", "");
            stompMessageString = stompMessageString.substring(genderEnd + 1);

            int statusStart = stompMessageString.indexOf(':');
            int statusEnd = stompMessageString.indexOf(',');
            String status = stompMessageString.substring(statusStart + 1, statusEnd).replace("\"", "");
            stompMessageString = stompMessageString.substring(statusEnd + 1);

            int imageURLStart = stompMessageString.indexOf(':');
            int imageURLEnd = stompMessageString.indexOf('}');
            String imageURL = stompMessageString.substring(imageURLStart + 1, imageURLEnd).replace("\"", "");
            stompMessageString = stompMessageString.substring(imageURLEnd + 2);

            if (!id.equals(currentUser.getId())) {
                userList.add(new User(id, userName, status, imageURL, gender));
            }
            if (stompMessageString.equals("")) {
                break;
            }
        }
        return userList;
    }

    public List<Chat> putChatListStompMessageToListOfChats(StompMessage stompMessage) {

        String stompMessageString = stompMessage.getContent();
        List<Chat> chatList = new ArrayList<>();

        while (true) {
            int idStart = stompMessageString.indexOf(':');
            int idEnd = stompMessageString.indexOf(',');
            String id = stompMessageString.substring(idStart + 1, idEnd);
            stompMessageString = stompMessageString.substring(idEnd + 1).replace("\"", "");

            int senderStart = stompMessageString.indexOf(':');
            int senderEnd = stompMessageString.indexOf(',');
            String sender = stompMessageString.substring(senderStart + 1, senderEnd).replace("\"", "");
            stompMessageString = stompMessageString.substring(senderEnd + 1);

            int receiverStart = stompMessageString.indexOf(':');
            int receiverEnd = stompMessageString.indexOf(',');
            String receiver = stompMessageString.substring(receiverStart + 1, receiverEnd).replace("\"", "");
            stompMessageString = stompMessageString.substring(receiverEnd + 1);

            int contentStart = stompMessageString.indexOf(':');
            int contentEnd = stompMessageString.indexOf('}');
            String content = stompMessageString.substring(contentStart + 1, contentEnd).replace("\"", "");
            stompMessageString = stompMessageString.substring(contentEnd + 2);

            chatList.add(new Chat(id, sender, receiver, content));
            if (stompMessageString.equals("")) {
                break;
            }
        }
        return chatList;
    }

}
