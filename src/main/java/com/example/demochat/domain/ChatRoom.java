package com.example.demochat.domain;

import com.example.demochat.utils.MessageSendUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@ToString
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

        if(chatMessage.getType() == MessageType.LEAVE) {
            remove(session);
//            session.sendMessage(new TextMessage("/chat/rooms")); //나간 사용자한테 전송(리다이렉트) <- 이 로직에서 예외처리 필요해서 Utils로 뺀거 이용
            String path = "/chat/rooms";
            try {
                MessageSendUtils.sendMessage(session, new TextMessage(objectMapper.writeValueAsString(path)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            chatMessage.setMessage(chatMessage.getWriter() + "님이 퇴장하였습니다.");
        }

        send(chatMessage, objectMapper); //나머지 브로드캐스트
    }

    //브로드 캐스트로 구현된 상태
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


    //퇴장
    public void remove(WebSocketSession target) {
        String targetId = target.getId();
        sessions.removeIf(session -> session.getId().equals(targetId));
    }


}
