package com.example.demochat.controller;

import com.example.demochat.domain.ChatRoom;
import com.example.demochat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatRoomRepository repository;
    private final AtomicInteger seq = new AtomicInteger(0);

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", repository.getChatRooms());
        return "/chat/room-list";
    }

    @GetMapping("room/{id}")
    public String room(@PathVariable String id, Model model) {
        log.info("id={}",id);

        ChatRoom room = repository.getChatRoom(id);
        log.info("room={}", room);
        model.addAttribute("room",room);
        model.addAttribute("member", "member"+seq.incrementAndGet()); //회원 이름 부여

        return "chat/room";
    }
}
