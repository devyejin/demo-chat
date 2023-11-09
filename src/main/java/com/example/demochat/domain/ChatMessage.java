package com.example.demochat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/*
    클라이언트와 주고받을 모델
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String chatRoomId;
    private String writer;
    private String message;
    private MessageType type;
}
