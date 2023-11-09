package com.example.demochat.domain;

import com.example.demochat.utils.MessageSendUtils;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ChatRoom {

    private String id;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>(); //Spring에서 WebSocket connection이 맺어진 세선
    //해당 session을 통해 메시지를 보낼 수 있음 session.sendMessage()


    //채팅방 생성
    public static ChatRoom create(@NotNull String name) {
        ChatRoom created = new ChatRoom();
        created.id = UUID.randomUUID().toString();
        created.name = name;

        return created;
    }

    public void handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) {

        if(chatMessage.getType() == MessageType.JOIN) {
            join(session); //커넥션 세션들 모아놓은 곳에 추가
            chatMessage.setMessage(chatMessage.getWriter() + "님이 입장하셨습니다."); //클라이언트로 메시지 전달 (ChatMessage객체 이용)
        }

        send(chatMessage, objectMapper);
    }

    private <T> void send(T messageObject, ObjectMapper objectMapper) {
        try {
            TextMessage message = new TextMessage(objectMapper.writeValueAsString(messageObject));
            sessions.parallelStream().forEach(session -> MessageSendUtils.sendMessage(session, message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void join(WebSocketSession session) {
        sessions.add(session); //커넥션 세션들 모아놓은 곳에 추가
    }


    //메시지 삭제


}
