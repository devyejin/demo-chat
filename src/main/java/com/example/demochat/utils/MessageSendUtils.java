package com.example.demochat.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@UtilityClass
@Slf4j
public class MessageSendUtils {

    public void sendMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message); //여기서 해당 세션으로 메시지가 감 => SSR로 해놓은 지금 클라이언트는 onMessage로 비동기로 응답 받고
        } catch (IOException e) {
           log.error(e.getMessage(), e);
        }
    }
}
