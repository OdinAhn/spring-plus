package org.example.expert.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.entity.ChatRoom;
import org.example.expert.domain.chat.repository.ChatRoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping
    public List<ChatRoom> getAll() {
        return chatRoomRepository.findAll();
    }

    @PostMapping
    public ChatRoom create(@RequestParam String name) {
        ChatRoom room = ChatRoom.builder().name(name).build();
        return chatRoomRepository.save(room);
    }
}
