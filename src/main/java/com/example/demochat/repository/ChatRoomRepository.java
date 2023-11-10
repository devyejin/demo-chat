package com.example.demochat.repository;

import com.example.demochat.domain.ChatRoom;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ChatRoomRepository {

    private final Map<String, ChatRoom> chatRoomMap;


    @Getter // <-- getChatRoom() 메서드에서 필드에 접근해야해서 필요
    private final Collection<ChatRoom> chatRooms;

    public ChatRoomRepository() {
        //테스트용 데이터
        chatRoomMap = Collections.unmodifiableMap(
                Stream.of(ChatRoom.create("1번방"),
                                ChatRoom.create("2번방"),
                                ChatRoom.create("3번방"))
                        .collect(Collectors.toMap(ChatRoom::getId, Function.identity()))); //Function.identity() 매개변수를 반환 여기선 chatRoom 객체겠지

        chatRooms = Collections.unmodifiableCollection(chatRoomMap.values());
    }

    public ChatRoom getChatRoom(String id) {
        return chatRoomMap.get(id);
    }

    public void remove(WebSocketSession session) {
        this.chatRooms.parallelStream().forEach(chatRoom -> chatRoom.remove(session));
    }
}
