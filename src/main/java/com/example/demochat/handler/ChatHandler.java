package com.example.demochat.handler;

import com.example.demochat.domain.ChatMessage;
import com.example.demochat.domain.ChatRoom;
import com.example.demochat.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
//@RequiredArgsConstructor
@Profile("!stomp")
@Slf4j // Log4j는 과거 유물
public class ChatHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private  ChatRoomRepository repository;

    //session에서 메시지를 수신했을 때 실행되는 메서드, 메시지 타입에 따라 Text, Binary 가능
    //지금은 TextWebSocketHandler 를 상속받아서 handleTextMessage,
    //이미지같은거 하고 싶다면 BinaryHandler도 추가 구현하면 가능할 듯
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
//        log.info("payload={}", payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom chatRoom = repository.getChatRoom(chatMessage.getChatRoomId());
        chatRoom.handleMessage(session, chatMessage, objectMapper);
    }

    //3-way-handshake 후 connection이 맺어진 다음 실행되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("3-way-handshake 후 connection 맺어짐!");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info("웹소켓 커넥션 종료!");
    }
}
