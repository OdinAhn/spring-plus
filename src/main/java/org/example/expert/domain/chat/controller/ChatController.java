package org.example.expert.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.redis.ChatRedisPublisher;
import org.example.expert.config.redis.RedisChatMessage;
import org.example.expert.entity.ChatMessage;
import org.example.expert.entity.ChatRoom;
import org.example.expert.domain.user.entity.User;
import org.example.expert.interceptor.AuthenticatedUser;
import org.example.expert.domain.chat.model.ChatMessageDto;
import org.example.expert.domain.chat.model.TypingIndicatorDto;
import org.example.expert.domain.chat.repository.ChatMessageRepository;
import org.example.expert.domain.chat.repository.ChatRoomRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRedisPublisher chatRedisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(ChatMessageDto dto, Principal principal) {

        User sender = AuthenticatedUser.fromPrincipal(principal);

        ChatRoom room = chatRoomRepository
            .findById(dto.getRoomId())
            .orElseThrow();

        ChatMessage message = new ChatMessage(sender, room, dto.getContent());
        chatMessageRepository.save(message);

        RedisChatMessage redisMessage = new RedisChatMessage(
            message.getChatRoom().getId(),
            message.getSender().getId(),
            message.getSender().getNickname(),
            message.getContent()
        );

        chatRedisPublisher.publish(room.getId(), redisMessage);

    }

    @MessageMapping("/chat.typing")
    public void typing(TypingIndicatorDto dto, Principal principal) {
        User user = AuthenticatedUser.fromPrincipal(principal);

        dto.setUserId(user.getId());
        dto.setUserName(user.getNickname());

        // 타이핑 상태를 해당 채팅방의 다른 사용자들에게 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/" + dto.getRoomId() + "/typing", dto);
    }
}