package com.example.demochat.config;

import com.example.demochat.handler.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Profile("!stomp")
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {



    private final  ChatHandler chatHandler; //WebSocketHandler 구현체
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //WebSocketHandler 구현체 chatHandler 등록
        //등록된 핸들러는 해당 endpoint로 HTTP 3-way-handshake 이뤄짐 -> Connection 관리
        registry.addHandler(chatHandler, "/ws/chat").setAllowedOriginPatterns("http://localhost:8080").withSockJS();

    }
}
